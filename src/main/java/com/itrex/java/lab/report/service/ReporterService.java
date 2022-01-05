package com.itrex.java.lab.report.service;

import com.itrex.java.lab.report.entity.CustomerReportDTO;
import com.itrex.java.lab.report.entity.OfferReportDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReporterService {

    List<CustomerReportDTO> findAllCustomer(LocalDate firstStartContractDate, LocalDate endStartContractDate,
                                            int startWithContractCount, int size);

    List<OfferReportDTO> getOfferReport(LocalDate firstStartContractDate, LocalDate endStartContractDate,
                                        int startWithContractId, int size);
}
