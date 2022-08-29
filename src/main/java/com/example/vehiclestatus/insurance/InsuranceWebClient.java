package com.example.vehiclestatus.insurance;

import java.util.function.*;
import org.slf4j.*;
import org.springframework.stereotype.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.*;

/**
 * GET https://insurance.com/accidents/report?vin={vin_number}
 * #### 200 Response Example:
 *     {
 * 	    "report": {
 * 		    "claims": 3 // Number of insurance claims for the vehicle
 *                }
 *     }
 * #### 404 - VIN number not found
 *  Class for creating mono to fetch data from Insurance public API
 */

@Component
public class InsuranceWebClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceWebClient.class);

    WebClient client = WebClient.create("http://insurance.com/accidents");

    public Mono<InsuranceInfo> consume(String vin) {

        Mono<InsuranceInfo> insuranceInfoMono = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/report/")
                        .queryParam("vin", vin)
                        .build())
                .retrieve()
                .bodyToMono(InsuranceInfo.class);
          return insuranceInfoMono;
    }
}
