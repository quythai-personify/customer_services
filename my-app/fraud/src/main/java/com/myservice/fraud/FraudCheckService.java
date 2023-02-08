package com.myservice.fraud;

import com.netflix.discovery.EurekaClient;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class FraudCheckService {
    private final FraudCheckHistoryRepository fraudCheckHistoryRepository;
    @Autowired
    private HttpClient httpClient;
    @Autowired
    private EurekaClient eurekaClient;

    public boolean isFraudulentCustomer(Integer customerId){
        fraudCheckHistoryRepository.save(
                FraudCheckHistory.builder()
                        .customerId(customerId)
                        .isFraudster(false)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        String serverId = "KAFKA-PRODUCER";
//        String homePageUrl = eurekaClient.getNextServerFromEureka(serverId, false).getHomePageUrl();
        String homePageUrl = "http://" + serverId + "/";
        JSONObject payload = new JSONObject();
        payload.put("request", "fraud check request for customer " + customerId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(homePageUrl + "api/v1/messages/publishMessage"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toJSONString()))
                .build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println);
        return false;
    }
}
