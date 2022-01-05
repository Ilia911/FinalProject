package com.itrex.java.lab.report.repository;

import com.itrex.java.lab.report.entity.ColumnDTO;
import com.itrex.java.lab.report.entity.ColumnType;
import com.itrex.java.lab.report.entity.CustomerReportDTO;
import com.itrex.java.lab.report.entity.ReportDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor

public class ReportRepositoryImpl implements ReportRepository{

    private static final String USER_ID = "userId";
    private static final String USER_NAME = "userName";
    private static final String TOTAL_CONTRACT_AMOUNT = "totalContractCount";
    private static final String TOTAL_CONTRACT_START_PRICE = "totalContractSum";
    private static final String FIRST_CONTRACT_START_DATE = "firstStartDate";
    private static final String LAST_CONTRACT_START_DATE = "lastStartDate";

    private EntityManager entityManager;

    @Override
    public ReportDTO getCustomerReport(LocalDate firstStartContractDate, LocalDate endStartContractDate,
                                       int startWithContractCount, int size) {

        Query query = entityManager.createQuery("select new com.itrex.java.lab.report.entity.CustomerReportDTO" +
                "(u.id, u.name, count(c.id), sum(c.startPrice), min(c.startDate), max(c.startDate)) " +
                "from User u join Contract c on u = c.owner " +
                "where c.startDate >= :firstStartContractDate and c.startDate <= :endStartContractDate " +
                "group by u.id, u.name having count(c.id) <= :startWithContractCount order by count(c.id) desc ", CustomerReportDTO.class);
        query.setMaxResults(size);
        query.setParameter("firstStartContractDate", firstStartContractDate);
        query.setParameter("endStartContractDate", endStartContractDate);
        query.setParameter("startWithContractCount", (long)startWithContractCount);

        List<CustomerReportDTO> resultList = query.getResultList();

        return createReportDTO(resultList);
    }

    private ReportDTO createReportDTO(List<CustomerReportDTO> resultList) {

        List<ColumnDTO> columnList = getColumnList();

        List<Map<String, Object>> values = getMapList(resultList);

        ReportDTO report = new ReportDTO();
        report.setColumns(columnList);
        report.setValues(values);

        return report;
    }

    private List<Map<String, Object>> getMapList(List<CustomerReportDTO> resultList) {

        List<Map<String, Object>> values = new ArrayList<>();

        for (CustomerReportDTO customerReportDTO : resultList) {

            Map<String, Object> map = new HashMap<>() {{
                put(USER_ID, customerReportDTO.getUserId());
                put(USER_NAME, customerReportDTO.getUserName());
                put(TOTAL_CONTRACT_AMOUNT, customerReportDTO.getTotalContractCount());
                put(TOTAL_CONTRACT_START_PRICE, customerReportDTO.getTotalContractSum());
                put(FIRST_CONTRACT_START_DATE, customerReportDTO.getFirstStartDate());
                put(LAST_CONTRACT_START_DATE, customerReportDTO.getLastStartDate());
            }};
            values.add(map);
        }
        return values;
    }

    private List<ColumnDTO> getColumnList() {
        ColumnDTO column1 = new ColumnDTO();
        column1.setId(USER_ID);
        column1.setTitle("Id of the User");
        column1.setType(ColumnType.NUMERIC);

        ColumnDTO column2 = new ColumnDTO();
        column2.setId(USER_NAME);
        column2.setTitle("Name of the User");
        column2.setType(ColumnType.STRING);

        ColumnDTO column3 = new ColumnDTO();
        column3.setId(TOTAL_CONTRACT_AMOUNT);
        column3.setTitle("Total Contracts amount");
        column3.setType(ColumnType.NUMERIC);

        ColumnDTO column4 = new ColumnDTO();
        column4.setId(TOTAL_CONTRACT_START_PRICE);
        column4.setTitle("Total Contract start price");
        column4.setType(ColumnType.NUMERIC);

        ColumnDTO column5 = new ColumnDTO();
        column5.setId(FIRST_CONTRACT_START_DATE);
        column5.setTitle("First contract start date");
        column5.setType(ColumnType.LOCAL_DATE);

        ColumnDTO column6 = new ColumnDTO();
        column6.setId(LAST_CONTRACT_START_DATE);
        column6.setTitle("Last contract start date");
        column6.setType(ColumnType.LOCAL_DATE);

        return Arrays.asList(column1, column2, column3, column4, column5, column6);
    }
}
