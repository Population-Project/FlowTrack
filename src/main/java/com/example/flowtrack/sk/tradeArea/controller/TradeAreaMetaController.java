package com.example.flowtrack.sk.tradeArea.controller;

import com.example.flowtrack.sk.tradeArea.TradeAreaMetaService;
import com.example.flowtrack.sk.tradeArea.dto.TradeAreaMetaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/areas")
public class TradeAreaMetaController {

    private final TradeAreaMetaService tradeAreaMetaService;

    // TODO : 그럼 항상 저 활성된 areaId를 먼저 조회하고 이걸 List에 저장한다음에 하나씩 api를 호출해야해?
    @GetMapping("/data")
    public TradeAreaMetaResponse getAreas() {
        return tradeAreaMetaService.getAvailableAreas();
    }
}

