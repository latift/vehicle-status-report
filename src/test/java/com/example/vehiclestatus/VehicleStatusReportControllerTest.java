package com.example.vehiclestatus;

import java.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.reactive.*;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.*;
import org.springframework.test.web.reactive.server.*;
import org.springframework.web.reactive.function.*;
import reactor.core.publisher.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = VehicleStatusReportController.class)
class VehicleStatusReportControllerTest {

    @MockBean
    VehicleStatusReportService vehicleStatusReportService;

    @Autowired
    private WebTestClient webClient;

    @Test
    void testGetVehicleStatusForEmptyFeatures() {
        VehicleStatusRequest vehicleStatusRequest = new VehicleStatusRequest();
        vehicleStatusRequest.setVin("vin-123");
        vehicleStatusRequest.setFeatures(new ArrayList<>());

        VehicleStatusResponse vehicleStatusResponse = new VehicleStatusResponse(
                "requestId_123",
                "vin_123",
                false,
                MaintenanceScore.average
        );

        Mockito.when(vehicleStatusReportService.getVehicleStatus(vehicleStatusRequest))
                .thenReturn(Mono.just(vehicleStatusResponse));

        webClient.post()
                .uri("/check")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(vehicleStatusRequest))
                .exchange()
                .expectStatus().isOk();

    }
}