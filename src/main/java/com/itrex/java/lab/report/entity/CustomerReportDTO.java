package com.itrex.java.lab.report.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerReportDTO {

    private int userId;
    private String userName;
    private long totalContractCount;
    private long totalContractSum;
    private LocalDate firstStartDate;
    private LocalDate lastStartDate;
}
