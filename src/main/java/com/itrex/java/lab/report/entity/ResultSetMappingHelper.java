package com.itrex.java.lab.report.entity;

//@MappedSuperclass
//@SqlResultSetMapping(
//        name="CustomerReportDTOMapping",
//        classes={
//                @ConstructorResult(
//                        targetClass= CustomerReportDTO.class,
//                        columns={
//                                @ColumnResult(name="id", type=Integer.class),
//                                @ColumnResult(name="name", type=String.class),
//                                @ColumnResult(name="contractAmount", type = Long.class),
//                                @ColumnResult(name = "totalContractSum", type = Long.class)})})
//@NamedNativeQueries({
//        @NamedNativeQuery(
//                name = "CustomerReport",
//                query = "select u.id, u.name, count(c.id) as total_contract_count, sum(c.start_price) total_contract_price from builder.user u " +
//                        "join builder.contract c on u.id = c.owner_id " +
//                        "where c.start_date >= '2022-01-01' and c.start_date <= '2022-02-01' " +
//                        "group by u.id, u.name " +
//                        "having count(c.id) < 9 " +
//                        "order by count(c.id) desc",
//                resultSetMapping = "CustomerReportDTOMapping"
//        )
//})
public abstract class ResultSetMappingHelper {
}
