package com.example.flowtrack.sk.tradeArea.controller;

import com.example.flowtrack.sk.tradeArea.dto.TradeAreaResponse;
import com.example.flowtrack.sk.tradeArea.service.TradeAreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@Profile("sk")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/congestion")
public class TradeAreaController {

    private final TradeAreaService congestionService;

    @GetMapping("/{areaId}")
    public TradeAreaResponse getCongestion(@PathVariable String areaId) {
        return congestionService.getCongestion(areaId);
    }
}
