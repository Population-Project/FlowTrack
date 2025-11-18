package com.example.flowtrack.seoulArea.service;

import com.example.flowtrack.common.util.ExcelReader;
import com.example.flowtrack.seoulArea.dto.AreaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AreaDataProcessor {

    private final ExcelReader excelReader;
    private final AreaService areaService;
    private final static String outPath = "seoul_area_11-3.csv";

    public void processExcel(String excelPath) throws Exception {
        List<String> areaNames = excelReader.readSeoulAreaNames(excelPath);

        List<AreaDto> results = new ArrayList<>();

        for (String areaName : areaNames) {
            AreaDto areaData = areaService.getAreaInfo(areaName);
            if (areaData != null) {
                results.add(areaData);
            }
            Thread.sleep(100);  // API 호출 제한
        }

        // 3. 수집된 데이터를 CSV로 저장
       areaService.saveAreas(results, outPath);
    }
}
