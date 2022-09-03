package com.example.vehiclestatus.insurance;

import com.example.vehiclestatus.exception.*;
import java.time.*;
import org.slf4j.*;
import org.springframework.http.*;
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

    WebClient client = WebClient.create("https://insurance.com/accidents");

    public Mono<InsuranceInfo> consume(String vin) {
        try {
            WebClient.ResponseSpec responseSpec = client.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/report")
                            .queryParam("vin", vin)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve();

            return responseSpec.bodyToMono(InsuranceInfo.class);
        }catch (Exception e){
            return Mono.error(new InsuranceServiceNotAvailable("Exception occurred while creating insurance mono" + e.getMessage()));
        }
    }

    public static void main(String[] args) {
        InsuranceWebClient insuranceWebClient = new InsuranceWebClient();
        Mono<InsuranceInfo> responseBody = insuranceWebClient.consume("4Y1SL65848Z411439");
        System.out.println(responseBody.block(Duration.ofSeconds(5)).toString());
    }
}
