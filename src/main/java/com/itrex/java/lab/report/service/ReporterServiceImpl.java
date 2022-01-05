package com.itrex.java.lab.report.service;

import com.itrex.java.lab.report.entity.ReportDTO;
import com.itrex.java.lab.report.repository.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class ReporterServiceImpl implements ReporterService {

    private ReportRepository repository;

    @Override
    @Transactional(readOnly = true)
    public ReportDTO findAllCustomer(LocalDate firstStartContractDate, LocalDate endStartContractDate,
                                     int startWithContractCount, int size) {

        return repository.getCustomerReport(firstStartContractDate, endStartContractDate, startWithContractCount, size);
    }
}
