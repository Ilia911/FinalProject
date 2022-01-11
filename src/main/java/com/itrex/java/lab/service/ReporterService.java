package com.itrex.java.lab.service;

import com.itrex.java.lab.entity.dto.CustomerReportDTO;
import com.itrex.java.lab.entity.dto.OfferReportDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReporterService {

    List<CustomerReportDTO> findAllCustomer(LocalDate firstStartContractDate, LocalDate endStartContractDate,
                                            int startWithContractCount, int size);

    List<OfferReportDTO> getOfferReport(LocalDate firstStartContractDate, LocalDate endStartContractDate,
                                        int startWithContractId, int size);
}
