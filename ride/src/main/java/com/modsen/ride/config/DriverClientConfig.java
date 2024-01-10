package com.modsen.ride.config;

import com.modsen.ride.controller.trace.TraceFeignInterceptor;
import com.modsen.ride.service.client.DriverClientErrorDecoder;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("trace")
@Configuration
public class DriverClientConfig {

    @Bean
    public RequestInterceptor traceInterceptor() {
        return new TraceFeignInterceptor();
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new DriverClientErrorDecoder();
    }
}
