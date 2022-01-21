package com.itrex.java.lab.repository.report;

import com.itrex.java.lab.entity.dto.CustomerReportDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepository {

    List<CustomerReportDTO> getCustomerReport(LocalDate startDate, LocalDate endDate, int count, int size);
}
