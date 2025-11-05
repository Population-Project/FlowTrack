package com.example.flowtrack.highwayroad.service;

import com.example.flowtrack.common.config.ExProps;
import com.example.flowtrack.highwayroad.dto.HighwayResponse;
import com.example.flowtrack.highwayroad.dto.HighwayInfoDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HighwayService {

    private final ExProps exProps;
    private final RestClient restClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * API 호출 후 CSV 저장만 수행
     */
    public void fetchAndSave(String outPath) throws Exception {
        HighwayResponse response = fetchHighwayInfo();
        if (response == null || response.getList() == null) {
            System.out.println("데이터 없음");
            return;
        }

        saveToCsv(response.getList(), outPath);
    }

    /**
     * API 호출 후 JSON → DTO 매핑
     */
    private HighwayResponse fetchHighwayInfo() throws Exception {
        String url = exProps.getBaseUrl()
                + "odtraffic/trafficAmountByCongest"
                + "?key=" + exProps.getApiKey()
                + "&type=json";

        String body = restClient.get()
                .uri(url)
                .header("Accept", "application/json")
                .retrieve()
                .body(String.class);

        return objectMapper.readValue(body.getBytes(StandardCharsets.UTF_8), HighwayResponse.class);
    }


    private void saveToCsv(List<HighwayInfoDto> data, String outPath) throws Exception {
        File file = new File(outPath);
        boolean fileExists = file.exists();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) { // append = true

            // 기존 파일이 없으면 헤더 추가, 있으면 한 줄 띄움
            if (!fileExists) {
                writer.println("stdDate,stdHour,routeNo,routeName,conzoneId,conzoneName,vdsId,trafficAmout,speed,shareRatio,timeAvg,grade,updownTypeCode");
            } else {
                writer.println();
            }

            for (HighwayInfoDto dto : data) {
                String line = String.join(",",
                        nz(dto.getStdDate()),
                        nz(dto.getStdHour()),
                        nz(dto.getRouteNo()),
                        nz(dto.getRouteName()),
                        nz(dto.getConzoneId()),
                        nz(dto.getConzoneName()),
                        nz(dto.getVdsId()),
                        nz(dto.getTrafficAmout()),
                        nz(dto.getSpeed()),
                        nz(dto.getShareRatio()),
                        nz(dto.getTimeAvg()),
                        nz(dto.getGrade()),
                        nz(dto.getUpdownTypeCode())
                );
                writer.println(line);
            }
            writer.flush();
        }

        System.out.println("고속도로 교통량 CSV 저장 완료: " + outPath);
    }



    private String nz(Object val) {
        return val == null ? "" : val.toString();
    }
}
