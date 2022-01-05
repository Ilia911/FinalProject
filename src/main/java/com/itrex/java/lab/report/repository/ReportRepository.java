package com.itrex.java.lab.report.repository;

import com.itrex.java.lab.report.entity.ReportDTO;

import java.time.LocalDate;

public interface ReportRepository {

    ReportDTO getCustomerReport(LocalDate firstStartContractDate,
                                      LocalDate endStartContractDate, int startWithContractCount, int size);
}
