package com.example.vehiclestatus;

import lombok.*;

/**
 * #### Response Body Example
 *
 *     {
 * 	    "request_id": "xxxx",
 * 	    "vin": "4Y1SL65848Z411439",
 * 	    "accident_free": false,
 * 	    "maintenance_score": "average" // Possible values: poor, average, good
 *     }
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleStatusResponse {

    private String request_id;
    private String vin;
    private boolean accident_free;
    private MaintenanceScore maintenance_score;

}
