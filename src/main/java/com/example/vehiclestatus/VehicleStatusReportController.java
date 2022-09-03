package com.example.vehiclestatus;

import com.example.vehiclestatus.exception.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.*;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.*;

@RestController
@RequestMapping("/check")
public class VehicleStatusReportController {

    private final VehicleStatusReportService vehicleStatusReportService;

    public VehicleStatusReportController(VehicleStatusReportService vehicleStatusReportService) {
        this.vehicleStatusReportService = vehicleStatusReportService;
    }

    @PostMapping()
    public Mono<VehicleStatusResponse> getVehicleStatus(@RequestBody VehicleStatusRequest vehicleStatusRequest) {
       if(!vehicleStatusRequest.getVin().isEmpty()) {
            return vehicleStatusReportService.getVehicleStatus(Mono.just(vehicleStatusRequest));
        }
        return Mono.error(new VinShouldNotBeEmpty("Vehicle Identity Number(vin) should not be empty"));
    }

}
