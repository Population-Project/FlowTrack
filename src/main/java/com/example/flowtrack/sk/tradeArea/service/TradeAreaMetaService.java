package com.example.flowtrack.sk.tradeArea;

import com.example.flowtrack.sk.tradeArea.dto.TradeAreaMetaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TradeAreaMetaService {

    @Value("${sk.api.appkey}")
    private String appKey;

    private static final String BASE_URL = "https://apis.openapi.sk.com/puzzle/place/meta/areas?offset=0&limit=100";

    public TradeAreaMetaResponse getAvailableAreas() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("appkey", appKey);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<TradeAreaMetaResponse> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.GET,
                entity,
                TradeAreaMetaResponse.class
        );

        return response.getBody();
    }
}
