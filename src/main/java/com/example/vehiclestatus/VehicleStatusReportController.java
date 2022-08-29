package com.example.vehiclestatus;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.*;

@RestController
@RequestMapping("/check")
public class VehicleStatusReportController {


    private final VehicleStatusReportService vehicleStatusReportService;

    public VehicleStatusReportController(VehicleStatusReportService vehicleStatusReportService) {
        this.vehicleStatusReportService = vehicleStatusReportService;
    }

    @PostMapping()
    public ResponseEntity<Mono<VehicleStatusResponse>> getVehicleStatus(@RequestBody VehicleStatusRequest vehicleStatusRequest) {
        Mono<VehicleStatusResponse> responseMono = vehicleStatusReportService.getVehicleStatus(vehicleStatusRequest);
        HttpStatus status = (responseMono != null) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<Mono<VehicleStatusResponse>>(responseMono, status);
    }

}
