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
            Customer quy = new Customer(
                    "Quy",
                    "Thai",
                    "quy.thai@personifyinc.com"
            );

            Customer toan = new Customer(
                    "Toan",
                    "Nguyen",
                    "toan.nguyen@personifyinc.com"
            );

            Customer thinh = new Customer(
                    "Thinh",
                    "Nguyen",
                    "thinh.nguyen@personifyinc.com"
            );

            repository.saveAll(Arrays.asList(quy,toan,thinh)
            );
        };
    }
}
