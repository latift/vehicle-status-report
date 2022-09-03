package com.example.vehiclestatus;

import com.example.vehiclestatus.exception.*;
import com.example.vehiclestatus.insurance.*;
import com.example.vehiclestatus.maintenance.*;
import java.rmi.server.*;
import java.util.*;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.*;
import reactor.core.publisher.*;

@Service
public class VehicleStatusReportService {

    private final MaintananceWebClient maintananceWebClient;

    private final InsuranceWebClient insuranceWebClient;

    public VehicleStatusReportService(MaintananceWebClient maintananceWebClient, InsuranceWebClient insuranceWebClient) {
        this.maintananceWebClient = maintananceWebClient;
        this.insuranceWebClient = insuranceWebClient;
    }

   public Mono<VehicleStatusResponse> getVehicleStatus(Mono<VehicleStatusRequest> vehicleStatusRequest) {

       Mono<MaintenanceInfo> maintenanceInfoMono = getMaintenanceStatus(vehicleStatusRequest);

       Mono<InsuranceInfo> insuranceInfoMono = getInsuranceStatus(vehicleStatusRequest);

       final Mono<VehicleStatusResponse> vehicleStatusResponse = vehicleStatusRequest
               .zipWith(insuranceInfoMono)
               .map((f) -> {
                   final VehicleStatusResponse vehicleStatusResponseInner = new VehicleStatusResponse();
                   vehicleStatusResponseInner.setVin(f.getT1().getVin());
                   vehicleStatusResponseInner.setRequest_id(UUID.randomUUID().toString());
                   vehicleStatusResponseInner.setAccident_free(f.getT2().getReportList().isEmpty());
                   return  vehicleStatusResponseInner;
               });

       final Mono<VehicleStatusResponse> vehicleStatusResponse2 =  vehicleStatusResponse
               .zipWith(maintenanceInfoMono)
               .map((t) -> {
                   final MaintenanceScore score;
                   switch (t.getT2().getMaintenance_frequency()) {
                       case "very-low":
                       case "low":
                           score = MaintenanceScore.poor;
                           break;
                       case "medium":
                           score = MaintenanceScore.average;
                           break;
                       case "high":
                           score = MaintenanceScore.good;
                           break;
                       default:
                           throw new UnknownMaintenanceScore("Maintenance Score is unknown");
                   }
                   return new VehicleStatusResponse(
                           t.getT1().getVin(),
                           t.getT1().getRequest_id(),
                           t.getT1().isAccident_free(),
                           score
                   );
               });
       return  vehicleStatusResponse2;
   }

    private Mono<MaintenanceInfo> getMaintenanceStatus(Mono<VehicleStatusRequest> vehicleStatusRequest) {
        List<String> features = vehicleStatusRequest.block().getFeatures();
        if (features.contains(VehicleStatusFeatures.maintenance.toString())) {
            Mono<MaintenanceInfo> maintenanceInfo = maintananceWebClient.consume(vehicleStatusRequest.block().getVin());
            return maintenanceInfo;
        }
        /*TODO: Below code should be reimplemented with retry mechanism or should throw exception.
        implemented like this for test purposes
        */
        return Mono.just(new MaintenanceInfo("very_low"));
    }

    private Mono<InsuranceInfo> getInsuranceStatus(Mono<VehicleStatusRequest> vehicleStatusRequest) {
        if(vehicleStatusRequest.block().getFeatures().contains(VehicleStatusFeatures.accident_free.toString())) {
            Mono<InsuranceInfo> insuranceInfoMono = insuranceWebClient.consume(vehicleStatusRequest.block().getVin());
            return insuranceInfoMono;
        }

        /*TODO: Below code should be reimplemented with retry mechanism or should throw exception.
        But implemented like this for now
        */
        return Mono.just(new InsuranceInfo(List.of()));
    }

}
