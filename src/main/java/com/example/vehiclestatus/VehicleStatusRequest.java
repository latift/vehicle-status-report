package com.example.vehiclestatus;

import java.util.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleStatusRequest {

    private String vin;
    private List<String> features;

}
