package com.example.flowtrack.seoulroad.service;

import com.example.flowtrack.seoulroad.dto.RoadInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RoadMultiThreadService {

    private final RoadService roadService;

    public void fetchAndSave(List<String> linkIds, String outPath) throws Exception {
        int threads = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        boolean append = true;
        File file = new File(outPath);
        boolean fileExists = file.exists();

        // 🔹 try-with-resources로 자동 닫기 보장
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, append))) {

            // 🔹 기존 파일이 없을 때만 헤더 작성
            if (!fileExists) {
                writer.println("link_id,road_name,st_node_nm,ed_node_nm,map_dist,reg_cd,speed,travel_time");
            } else {
                // 기존 파일이 있으면 구분을 위해 한 줄 띄움
                writer.println();
            }
            writer.flush();

            // 🔹 각 linkId 작업을 스레드 풀에 제출
            for (String linkId : linkIds) {
                executor.submit(() -> {
                    try {
                        RoadInfoDto info = roadService.getRoadInfo(linkId);
                        if (info != null) {
                            String line = String.join(",",
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

            // 🔹 스레드 풀 종료 및 대기
            executor.shutdown();
            if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
                executor.shutdownNow(); // 강제 종료
            }

        } // 🔹 try-with-resources가 자동으로 writer.close() 호출

        System.out.println("모든 데이터 저장 완료: " + outPath);
    }

    private String nz(Object o) {
        return (o == null) ? "" : o.toString().replace(",", " ");
    }
}