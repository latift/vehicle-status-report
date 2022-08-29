package com.example.vehiclestatus.maintenance;

import org.slf4j.*;
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
        Mono<MaintenanceInfo> mono =  client.get()
          .uri("/{vin}", vin)
          .retrieve()
          .bodyToMono(MaintenanceInfo.class);
        return mono;
    }

}
