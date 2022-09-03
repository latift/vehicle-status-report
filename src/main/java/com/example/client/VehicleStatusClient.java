package com.example.client;

import com.example.vehiclestatus.*;
import com.example.vehiclestatus.logging.*;
import java.util.*;
import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.http.client.reactive.*;
import org.springframework.util.*;
import org.springframework.web.reactive.function.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.*;


public class VehicleStatusClient {

    private static final Logger logger = LoggerFactory.getLogger(VehicleStatusClient.class);

    private final WebClient client=WebClient.create("http://localhost:8080");

    private final VehicleStatusRequest vehicleStatusRequest;

    public VehicleStatusClient() {
        vehicleStatusRequest = new VehicleStatusRequest(
                "4Y1SL65848Z411439",
                List.of(VehicleStatusFeatures.accident_free.toString(), VehicleStatusFeatures.maintenance.toString())
        );
    }

    public Mono<String> check() {

        LinkedMultiValueMap map = new LinkedMultiValueMap();

        BodyInserter<MultiValueMap<String, Object>, ClientHttpRequest> inserter2
                = BodyInserters.fromMultipartData(map);

        return this.client.post()
                .uri("/check")
                .body(Mono.just(vehicleStatusRequest), VehicleStatusRequest.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(VehicleStatusResponse.class)
                .map(VehicleStatusResponse::toString);
    }

    public static void main(String[] args) {
        VehicleStatusClient vehicleStatusClient = new VehicleStatusClient();
        logger.info("VEHICLE STATUS REPORT APP: Calling backend");
        String result = vehicleStatusClient.check().block();
        logger.info("VEHICLE STATUS REPORT APP: Got response To http://car-checker.com/check :" + result);
    }
}
