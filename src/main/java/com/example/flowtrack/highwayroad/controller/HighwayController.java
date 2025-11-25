package com.example.flowtrack.highwayroad.controller;

import com.example.flowtrack.highwayroad.service.HighwayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/highway")
@RequiredArgsConstructor
public class HighwayController {

    private final HighwayService highwayService;
    private final static String outPath = "highway_11-4.csv";

    @GetMapping
    public String getHighwayInfo() throws Exception {
        highwayService.fetchAndSave(outPath);
        return "고속도로 교통량 CSV 저장 완료";
    }
}