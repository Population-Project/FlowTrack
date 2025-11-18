package com.example.flowtrack.seoulroad.service;

import com.example.flowtrack.common.util.ExcelReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoadDataProcessor {

    private final ExcelReader excelReader;
    private final RoadMultiThreadService roadMultiThreadService;
    private final RoadService roadService;
    private final static String outPath = "seoul_road_11-3.csv";

    /**
     * 엑셀에서 linkId 읽고 → TrafficService 호출 → 결과 리스트 반환
     */
    public void processExcel(String excelPath) throws Exception {
        List<String> linkIds = excelReader.readSeoulRoadLinkIds(excelPath);
        roadMultiThreadService.fetchAndSave(linkIds, outPath);

        for (String linkId : linkIds) {
            roadService.getRoadInfo(linkId);
            Thread.sleep(100);
        }
    }
}
