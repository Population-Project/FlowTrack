package com.example.flowtrack.seoulroad.service;

import com.example.flowtrack.seoulroad.dto.RoadInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class RoadMultiThreadService {

    private final RoadService roadService;
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void fetchAndSave(List<String> linkIds, String outPath) throws Exception {
        int threads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        boolean append = true;
        File file = new File(outPath);
        boolean fileExists = file.exists();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, append))) {

            if (!fileExists) {
                writer.println("API 호출 시간,link_id,road_name,st_node_nm,ed_node_nm,map_dist,reg_cd,speed,travel_time");
            } else {
                writer.println();
            }
            writer.flush();

            for (String linkId : linkIds) {
                executor.submit(() -> {
                    try {
                        RoadInfoDto info = roadService.getRoadInfo(linkId);
                        if (info != null) {
                            String timestamp = LocalDateTime.now().format(TIME_FORMATTER);

                            String line = String.join(",",
                                    timestamp,
                                    nz(info.getLinkId()),
                                    nz(info.getRoadName()),
                                    nz(info.getStNodeNm()),
                                    nz(info.getEdNodeNm()),
                                    nz(info.getMapDist()),
                                    nz(info.getRegCd()),
                                    nz(info.getSpeed()),
                                    nz(info.getTravelTime())
                            );

                            synchronized (writer) {
                                writer.println(line);
                                writer.flush();
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error fetching linkId=" + linkId + ": " + e.getMessage());
                    }
                });
            }

            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }

        }

        System.out.println("모든 데이터 저장 완료: " + outPath);
    }

    private String nz(Object o) {
        return (o == null) ? "" : o.toString().replace(",", " ");
    }
}
