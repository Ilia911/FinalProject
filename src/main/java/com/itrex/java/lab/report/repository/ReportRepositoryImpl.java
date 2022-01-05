package com.itrex.java.lab.report.repository;

import com.itrex.java.lab.report.entity.CustomerReportDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Repository
@AllArgsConstructor

public class ReportRepositoryImpl implements ReportRepository {

    private EntityManager entityManager;

    @Override
    public List<CustomerReportDTO> getCustomerReport(LocalDate firstStartContractDate, LocalDate endStartContractDate,
                                                     int startWithContractCount, int size) {

        Query query = entityManager.createQuery("select new com.itrex.java.lab.report.entity.CustomerReportDTO" +
                "(u.id, u.name, count(c.id), sum(c.startPrice), min(c.startDate), max(c.startDate)) " +
                "from User u join Contract c on u = c.owner " +
                "where u.role = 'CUSTOMER' and c.startDate >= :firstStartContractDate " +
                "and c.startDate <= :endStartContractDate " +
                "group by u.id, u.name having count(c.id) <= :startWithContractCount order by count(c.id) desc ", CustomerReportDTO.class);
        query.setMaxResults(size);
        query.setParameter("firstStartContractDate", firstStartContractDate);
        query.setParameter("endStartContractDate", endStartContractDate);
        query.setParameter("startWithContractCount", (long) startWithContractCount);

        return (List<CustomerReportDTO>) query.getResultList();
    }
}
