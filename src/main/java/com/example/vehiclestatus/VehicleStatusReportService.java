package com.example.vehiclestatus;

import com.example.vehiclestatus.insurance.*;
import com.example.vehiclestatus.logging.*;
import com.example.vehiclestatus.maintenance.*;
import java.util.*;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.Logger;
import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.*;

@Service
public class VehicleStatusReportService {

    private final MaintananceWebClient maintananceWebClient;

    private final InsuranceWebClient insuranceWebClient;

    VehicleStatusResponse vehicleStatusResponse;

    private static final Logger logger = LogManager.getLogger(VehicleStatusReportService.class);

    public VehicleStatusReportService(MaintananceWebClient maintananceWebClient, InsuranceWebClient insuranceWebClient) {
        this.maintananceWebClient = maintananceWebClient;
        this.insuranceWebClient = insuranceWebClient;
    }


    private Mono<MaintenanceInfo> getMaintenanceStatus(VehicleStatusRequest vehicleStatusRequest) {
        vehicleStatusResponse = new VehicleStatusResponse();
        if (vehicleStatusRequest.getFeatures().contains(VehicleStatusFeatures.maintenance)) {
            Mono<MaintenanceInfo> maintenanceInfoMono = maintananceWebClient.consume(vehicleStatusRequest.getVin());
            return maintenanceInfoMono;
        }
        return Mono.empty();
    }

    private Mono<InsuranceInfo> getInsuranceStatus(VehicleStatusRequest vehicleStatusRequest) {
        if(vehicleStatusRequest.getFeatures().contains(VehicleStatusFeatures.accident_free)) {
            Mono<InsuranceInfo> insuranceInfoMono = insuranceWebClient.consume(vehicleStatusRequest.getVin());
            return insuranceInfoMono;
        }
        return Mono.empty();
    }

    public Mono<VehicleStatusResponse> getVehicleStatus(VehicleStatusRequest vehicleStatusRequest) {

        String uniqueID = UUID.randomUUID().toString();
        vehicleStatusResponse = new VehicleStatusResponse();

        Mono<MaintenanceInfo> res1 = getMaintenanceStatus(vehicleStatusRequest);
        res1.subscribe(this::maintenanceInfoSuccessCallback,this::maintenanceInfoErrorCallback,this::maintenanceInfoOnComplete);


        Mono<InsuranceInfo> res2 = getInsuranceStatus(vehicleStatusRequest);
        res2.subscribe(this::insuranceSuccessCalback, this::insuranceErrorCalback,this::insuranceOnComplete);

        vehicleStatusResponse.setVin(vehicleStatusRequest.getVin());
        vehicleStatusResponse.setRequest_id(uniqueID);

        Mono<VehicleStatusResponse> vehicleStatusResponseMono = null;
        return  vehicleStatusResponseMono;

    }



    private void maintenanceInfoSuccessCallback(MaintenanceInfo maintenanceInfoResp) {
        logger.info("Success maintenanceInfoSuccessCallback");
        logger.info(maintenanceInfoResp);
        if (maintenanceInfoResp.getMaintenance_frequency().equals("very_low") || maintenanceInfoResp.getMaintenance_frequency().equals("low")) {
            vehicleStatusResponse.setMaintenance_score(MaintenanceScore.poor);
        } else if (maintenanceInfoResp.getMaintenance_frequency().equals("medium")) {
            vehicleStatusResponse.setMaintenance_score(MaintenanceScore.average);
        } else if (maintenanceInfoResp.getMaintenance_frequency().equals("high")) {
            vehicleStatusResponse.setMaintenance_score(MaintenanceScore.good);
        }
    }

    private void maintenanceInfoErrorCallback(Throwable maintenanceInfoThrowable) {
        logger.info("Error maintenanceInfoErrorCallback" );
        logger.info(maintenanceInfoThrowable.getMessage());
    }


    private void maintenanceInfoOnComplete() {
        logger.info("Completed Maintenance");
    }


    private void insuranceSuccessCalback(InsuranceInfo insuranceInfoResp) {
        logger.info("Success insuranceSuccessCalback" );
        vehicleStatusResponse.setAccident_free(insuranceInfoResp.getReportList().isEmpty());
    }

    private void insuranceErrorCalback(Throwable insuranceInfoThrowable) {
        logger.info("Error insuranceErrorCalback" );
    }

    private void insuranceOnComplete() {
        logger.info("Completed insuranceOnComplete" );
    }

}
