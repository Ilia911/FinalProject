package com.itrex.java.lab.repository.report.impl;

import com.itrex.java.lab.entity.dto.CustomerReportDTO;
import com.itrex.java.lab.repository.report.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepository {

    private static final String REPORT_QUERY = "select new com.itrex.java.lab.entity.dto.CustomerReportDTO" +
            "(u.id, u.name, count(c.id), sum(c.startPrice), min(c.startDate), max(c.startDate)) " +
            "from User u join Contract c on u = c.owner " +
            "where u.role = 'CUSTOMER' and c.startDate between :startDate and :endDate " +
            "group by u.id, u.name having count(c.id) <= :count order by count(c.id) desc ";

    private final EntityManager entityManager;

    @Override
    public List<CustomerReportDTO> getCustomerReport(LocalDate startDate, LocalDate endDate,
                                                     int startWithContractCount, int size) {

        Query query = entityManager.createQuery(REPORT_QUERY, CustomerReportDTO.class);
        query.setMaxResults(size);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("count", (long) startWithContractCount);

        return (List<CustomerReportDTO>) query.getResultList();
    }
}
