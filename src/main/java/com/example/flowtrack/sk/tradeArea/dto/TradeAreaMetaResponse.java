package com.example.flowtrack.sk.tradeArea.dto;

import lombok.Data;
import java.util.List;

@Data
public class TradeAreaMetaResponse {

    private Status status;
    private List<Contents> contents;

    @Data
    public static class Status {
        private String code;
        private String message;
        private int totalCount;
        private int offset;
        private int limit;
    }

    @Data
    public static class Contents {
        private String areaId;
        private String areaName;
    }
}
