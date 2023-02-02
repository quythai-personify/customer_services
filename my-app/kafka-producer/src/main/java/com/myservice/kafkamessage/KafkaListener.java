package com.myservice.kafkamessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaListener {
    @org.springframework.kafka.annotation.KafkaListener(
            topics = "sample",
            groupId = "groupId"
    )
    void listener(@Payload(required = false) String data){
        if (data != null){
            System.out.println("Listener received: " + data);
        }
        else {
            System.out.println("Receive null message");
        }
    }
}
