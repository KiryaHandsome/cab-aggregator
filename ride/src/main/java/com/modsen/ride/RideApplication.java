package com.modsen.ride;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class RideApplication {

    public static void main(String[] args) {
        SpringApplication.run(RideApplication.class, args);
    }

}
