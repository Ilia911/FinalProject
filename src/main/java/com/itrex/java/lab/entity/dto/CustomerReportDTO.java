package com.itrex.java.lab.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CustomerReportDTO {

    private int userId;
    private String userName;
    private long totalContractCount;
    private long totalContractSum;
    private LocalDate firstStartDate;
    private LocalDate lastStartDate;
}
