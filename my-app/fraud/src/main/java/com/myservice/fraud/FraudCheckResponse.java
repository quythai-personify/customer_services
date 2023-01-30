package com.myservice.fraud;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FraudCheckResponse(@JsonProperty("isFraudster") Boolean isFraudster) {
}
