package com.example.flowtrack.seoulroad.service;

import com.example.flowtrack.common.config.SeoulProps;
import com.example.flowtrack.seoulroad.dto.LiveRoadResponse;
import com.example.flowtrack.seoulroad.dto.RoadInfoDto;
import com.example.flowtrack.seoulroad.dto.RoadMetaResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class RoadService {

    private final SeoulProps seoulProps;
    private final RestClient restClient;
    private final XmlMapper xmlMapper = new XmlMapper();

    private int startIndex = 1;
    private int endIndex = 1;

    @Bean
    public RestClient restClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); // 3초 연결 제한
        factory.setReadTimeout(5000);    // 5초 응답 제한

        return RestClient.builder()
                .requestFactory(factory)
                .build();
    }
    
    public RoadInfoDto getRoadInfo(String linkId) throws Exception {
        LiveRoadResponse live = fetchLiveRoad(linkId);
        RoadMetaResponse meta = fetchRoadMeta(linkId);

        Double speed = null;
        Integer travelTime = null;
        if (live != null && live.getRows() != null && !live.getRows().isEmpty()) {
            speed = live.getRows().get(0).getPrcsSpd();
            travelTime = live.getRows().get(0).getPrcsTrvTime();
        }

        return new RoadInfoDto(
                linkId,
                meta != null ? meta.getRoadName() : null,
                meta != null ? meta.getStNodeNm() : null,
                meta != null ? meta.getEdNodeNm() : null,
                meta != null ? meta.getMapDist() : null,
                meta != null ? meta.getRegCd() : null,
                speed,
                travelTime
        );
    }

    private LiveRoadResponse fetchLiveRoad(String linkId) throws Exception {
        String url = UriComponentsBuilder.fromHttpUrl(seoulProps.getBaseUrl())
                .pathSegment(seoulProps.getApiKey(), "xml", "TrafficInfo", String.valueOf(startIndex), String.valueOf(endIndex), linkId)
                .toUriString();

        String xml = restClient.get().uri(url).retrieve().body(String.class);
        JsonNode wrap = xmlMapper.readTree(xml.getBytes(StandardCharsets.UTF_8));
        String raw = wrap.path("raw").asText();
        if (!StringUtils.hasText(raw)) raw = xml;

        return xmlMapper.readValue(raw.getBytes(StandardCharsets.UTF_8), LiveRoadResponse.class);
    }

    private RoadMetaResponse fetchRoadMeta(String linkId) throws Exception {
        String url = UriComponentsBuilder.fromHttpUrl(seoulProps.getBaseUrl())
                .pathSegment(seoulProps.getApiKey(), "xml", "LinkInfo", String.valueOf(startIndex), String.valueOf(endIndex), linkId)
                .toUriString();

        String xml = restClient.get().uri(url).retrieve().body(String.class);

        JsonNode wrap = xmlMapper.readTree(xml.getBytes(StandardCharsets.UTF_8));
        String raw = wrap.path("raw").asText();
        if (!StringUtils.hasText(raw)) raw = xml;

        JsonNode doc = xmlMapper.readTree(raw.getBytes(StandardCharsets.UTF_8));
        JsonNode row = doc.path("row");
        if (row.isArray() && row.size() > 0) row = row.get(0);

        return xmlMapper.treeToValue(row, RoadMetaResponse.class);
    }
}
