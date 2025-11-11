package com.example.flowtrack.sk.tradeArea.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class TradeArea {
    @Id @GeneratedValue
    private Long id;

    private String areaId;

    private String areaName;

    private double congestion;

    private int congestionLevel;

    private String datetime;
}