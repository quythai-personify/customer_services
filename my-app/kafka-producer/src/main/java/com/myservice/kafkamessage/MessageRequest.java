package com.myservice.kafkamessage;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageRequest(@JsonProperty("request") String message) {
}
