package com.myservice.fraud;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/fraud-check")
@AllArgsConstructor
@Slf4j
public class FraudController {
    private final FraudCheckService fraudCheckService;
    @GetMapping(path = "{customerId}")
    public FraudCheckResponse isFraudster(
            @PathVariable("customerId") Integer customerId) {
        Boolean isFraudulentCustomer = fraudCheckService.
                isFraudulentCustomer(customerId);
        log.info("fraud check request for customer {}", customerId);
        return  new FraudCheckResponse(isFraudulentCustomer);
    }
}
