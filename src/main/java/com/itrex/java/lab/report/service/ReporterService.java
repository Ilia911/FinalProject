package com.itrex.java.lab.report.service;

import com.itrex.java.lab.report.entity.CustomerReportDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReporterService {

    List<CustomerReportDTO> findAllCustomer(LocalDate firstStartContractDate, LocalDate endStartContractDate,
                                            int startWithContractCount, int size);
}
