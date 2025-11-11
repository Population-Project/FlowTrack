package com.example.flowtrack.sk.tradeArea.dto;

import lombok.Data;

@Data
public class TradeAreaResponse {
    private Status status;
    private Contents contents;

    @Data
    public static class Status {
        private String code;
        private String message;
        private int totalCount;
    }

    @Data
    public static class Contents {
        private String areaId;
        private String areaName;
        private Rltm rltm;

        @Data
        public static class Rltm {
            private double congestion;
            private int congestionLevel;
            private String datetime;
        }
    }
}
