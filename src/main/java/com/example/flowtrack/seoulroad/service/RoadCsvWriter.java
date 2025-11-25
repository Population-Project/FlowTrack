package com.example.flowtrack.seoulroad.service;

import com.example.flowtrack.seoulroad.dto.RoadInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoadCsvWriter {

    private final RoadService roadService;
    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void writeCsv(List<String> linkIds, String outPath) throws Exception {

        File file = new File(outPath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {

            writer.println("API 호출 시간,link_id,road_name,st_node_nm,ed_node_nm,map_dist,reg_cd,speed,travel_time");

            for (String linkId : linkIds) {

                try {
                    RoadInfoDto info = roadService.getRoadInfo(linkId);
                    if (info == null) {
                        System.err.println("[WARN] 응답 없음 → linkId = " + linkId);
                        continue;
                    }

                    String timestamp = LocalDateTime.now().format(TIME_FORMAT);

                    writer.println(String.join(",",
                            timestamp,
                            nz(info.getLinkId()),
                            nz(info.getRoadName()),
                            nz(info.getStNodeNm()),
                            nz(info.getEdNodeNm()),
                            nz(info.getMapDist()),
                            nz(info.getRegCd()),
                            nz(info.getSpeed()),
                            nz(info.getTravelTime())
                    ));

                    writer.flush();
                    Thread.sleep(50);

                } catch (Exception e) {
                    System.err.println("[ERROR] linkId 실패: " + linkId + " → " + e.getMessage());
                }
            }
        }

        System.out.println("CSV 저장 완료 → " + outPath);
    }

    private String nz(Object o) {
        return o == null ? "" : o.toString().replace(",", " ");
    }
}
