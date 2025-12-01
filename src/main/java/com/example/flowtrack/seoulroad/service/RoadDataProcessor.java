package com.example.flowtrack.seoulroad.service;

import com.example.flowtrack.common.util.ExcelReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoadDataProcessor {

    private final ExcelReader excelReader;
    private final RoadCsvWriter csvWriter;
    private final static String outPath = "seoul_road_12-1.csv";

    /**
     * 엑셀에서 linkId 읽고 → TrafficService 호출 → 결과 리스트 반환
     */
    public void processExcel(String excelPath) throws Exception {
        List<String> linkIds = excelReader.readSeoulRoadLinkIds(excelPath);
        csvWriter.writeCsv(linkIds, outPath);
    }
}
