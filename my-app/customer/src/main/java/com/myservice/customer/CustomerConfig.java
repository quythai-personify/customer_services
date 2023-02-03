package com.myservice.customer;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

@Configuration
public class CustomerConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository repository){
        return args -> {
            Customer quy = Customer.builder()
                    .firstName("Quy")
                    .lastName("Thai")
                    .email("quy.thai@personifyinc.com")
                    .build();
            Customer toan = Customer.builder()
                    .firstName("Toan")
                    .lastName("Nguyen")
                    .email("toan.nguyen@personifyinc.com")
                    .build();
            Customer thinh = Customer.builder()
                    .firstName("Thinh")
                    .lastName("Nguyen")
                    .email("thinh.nguyen@personifyinc.com")
                    .build();

            repository.saveAll(Arrays.asList(quy,toan,thinh));
        };
    }
}
