package com.example.vehiclestatus;

import com.example.vehiclestatus.maintenance.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.autoconfigure.mongo.*;
import reactor.core.publisher.*;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class VehicleStatusApplication {

    public static void main(String[] args) {

        SpringApplication.run(VehicleStatusApplication.class, args);

    }

}
