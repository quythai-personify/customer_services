package com.myservice.kafkamessage;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/messages")
public class MessageController {
    private KafkaTemplate<String, String> kafkaTemplate;

    public MessageController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping(
            value = "/publishMessage", consumes = "application/json", produces = "application/json")
    public void publish(@RequestBody MessageRequest request){
        kafkaTemplate.send("sample", request.message());
    }
}
