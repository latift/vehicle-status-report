package com.example.vehiclestatus.maintenance;

import com.example.vehiclestatus.*;
import com.example.vehiclestatus.exception.*;
import java.time.*;
import org.slf4j.*;
import org.springframework.boot.*;
import org.springframework.http.*;
import org.springframework.stereotype.*;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.*;

/**
 * ### Maintenance
 * GET https://topgarage.com/cars/{vin}
 * #### 200 Response Example:
 *     {
 * 	    "maintenance_frequency": "low" // Possible values: very-low, low, medium, high
 *     }
 *
 *     Class for creating mono to fetch data from Topgarage public API
 */
@Component
public class MaintananceWebClient {

    private static final Logger logger = LoggerFactory.getLogger(MaintananceWebClient.class);

    WebClient client = WebClient.create("https://topgarage.com/cars");

    public Mono<MaintenanceInfo> consume(String vin) {
        try {
            WebClient.ResponseSpec responseSpec = client.get()
                    .uri("/{vin}", vin)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve();
            return responseSpec.bodyToMono(MaintenanceInfo.class);
        }catch (Exception e){
            return Mono.error(new MaintenanceServiceNotAvailable("Exception occurred while creating maintenance mono" + e.getMessage()));
        }
    }

    public static void main(String[] args) {
        MaintananceWebClient maintananceWebClient = new MaintananceWebClient();
        Mono<MaintenanceInfo> responseBody = maintananceWebClient.consume("4Y1SL65848Z411439");
        System.out.println(responseBody.block(Duration.ofSeconds(5)).toString());
    }

}
