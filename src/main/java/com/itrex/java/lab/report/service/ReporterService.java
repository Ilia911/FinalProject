package com.itrex.java.lab.report.service;

import com.itrex.java.lab.report.entity.ReportDTO;

import java.time.LocalDate;

public interface ReporterService {

    ReportDTO findAllCustomer(LocalDate firstStartContractDate, LocalDate endStartContractDate,
                              int startWithContractCount, int size);
}
