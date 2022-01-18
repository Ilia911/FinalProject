package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.CustomerReportPageParameter;
import com.itrex.java.lab.entity.OfferReportPageParameter;
import com.itrex.java.lab.entity.dto.CustomerReportDTO;
import com.itrex.java.lab.entity.dto.OfferReportDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.report.ReportRepository;
import com.itrex.java.lab.repository.report.ReportRepositoryNativeSQL;
import com.itrex.java.lab.service.ReporterService;
import com.itrex.java.lab.service.util.CustomerReportPageParameterValidator;
import com.itrex.java.lab.service.util.OfferReportPageParameterValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporterServiceImpl implements ReporterService {

    private final ReportRepository repository;
    private final ReportRepositoryNativeSQL repositoryNativeSQL;
    private final CustomerReportPageParameterValidator customerReportPageParameterValidator;
    private final OfferReportPageParameterValidator offerReportPageParametersValidator;

    @Override
    @Transactional(readOnly = true)
    public List<CustomerReportDTO> findAllCustomer(CustomerReportPageParameter parameter) throws ServiceException {

        customerReportPageParameterValidator.validatePageParameters(parameter);

        return repository.getCustomerReport(LocalDate.parse(parameter.getFirstStartContractDate()),
                LocalDate.parse(parameter.getLastStartContractDate()), parameter.getStartWithContractCount(),
                parameter.getSize());
    }

    @Override
    public List<OfferReportDTO> getOfferReport(OfferReportPageParameter parameter) throws ServiceException {

        offerReportPageParametersValidator.validate(parameter);

        return repositoryNativeSQL.getOfferReport(LocalDate.parse(parameter.getFirstStartContractDate()),
                LocalDate.parse(parameter.getLastStartContractDate()), parameter.getStartWithContractId(),
                parameter.getSize());
    }
}
