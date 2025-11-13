package com.example.flowtrack.sk.tradeArea.service;

import com.example.flowtrack.sk.tradeArea.dto.TradeAreaResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Profile("sk")
@Service
public class TradeAreaService {

    @Value("${sk.api.appkey}")
    private String appKey;

    private static final String BASE_URL = "https://apis.openapi.sk.com/puzzle/place/congestion/rltm/areas/";

    public TradeAreaResponse getCongestion(String areaId) {
        String url = BASE_URL + areaId;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("appkey", appKey);
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<TradeAreaResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                TradeAreaResponse.class
        );

        return response.getBody();
    }
}
