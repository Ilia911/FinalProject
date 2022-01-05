package com.itrex.java.lab.report.service;

import com.itrex.java.lab.report.entity.CustomerReportDTO;
import com.itrex.java.lab.report.entity.OfferReportDTO;
import com.itrex.java.lab.report.repository.ReportRepository;
import com.itrex.java.lab.report.repository.ReportRepositoryNativeSQL;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ReporterServiceImpl implements ReporterService {

    private ReportRepository repository;
    private ReportRepositoryNativeSQL repositoryNativeSQL;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerReportDTO> findAllCustomer(LocalDate firstStartContractDate, LocalDate endStartContractDate,
                                                   int startWithContractCount, int size) {

        return repository.getCustomerReport(firstStartContractDate, endStartContractDate, startWithContractCount, size);
    }

    @Override
    public List<OfferReportDTO> getOfferReport(LocalDate firstStartContractDate, LocalDate endStartContractDate, int startWithContractId, int size) {
//        return repository.getOfferReport(firstStartContractDate, endStartContractDate, startWithContractId, size);
        return repositoryNativeSQL.getOfferReport(firstStartContractDate, endStartContractDate, startWithContractId, size);
    }
}
