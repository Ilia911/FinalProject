package com.itrex.java.lab.repository.report;

import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.dto.OfferReportDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ReportRepositoryNativeSQL extends JpaRepository<Offer, Integer> {

    @Query(value = "SELECT c.id              AS contractId,\n" +
            "       c.description            AS contractDescription,\n" +
            "       c.start_price            AS startPrice,\n" +
            "       u_o_min_price.owner_id   AS offerOwnerId,\n" +
            "       u_o_min_price.owner_name AS offerOwnerName,\n" +
            "       offer_id                 AS offerId,\n" +
            "       min_price                AS minOfferPrice\n" +
            "FROM   (SELECT *\n" +
            "        FROM   builder.contract\n" +
            "        WHERE  start_date BETWEEN :startDate AND :endDate) AS c\n" +
            "       JOIN (SELECT u.id     owner_id,\n" +
            "                    u.name   owner_name,\n" +
            "                    o2.id    offer_id,\n" +
            "                    o2.contract_id,\n" +
            "                    o2.price min_price\n" +
            "             FROM   builder.user u\n" +
            "                    JOIN (SELECT o.id,\n" +
            "                                 o.offer_owner_id,\n" +
            "                                 o.contract_id,\n" +
            "                                 o.price\n" +
            "                          FROM   builder.offer o\n" +
            "                                 JOIN (SELECT o.contract_id,\n" +
            "                                              Min(o.price) price\n" +
            "                                       FROM   (SELECT *\n" +
            "                                               FROM   builder.offer\n" +
            "                                               WHERE  offer.contract_id > :startId) o\n" +
            "                                       GROUP  BY o.contract_id) AS o_min_price\n" +
            "                                   ON o.contract_id = o_min_price.contract_id\n" +
            "                                      AND o.price = o_min_price.price) o2\n" +
            "                      ON u.id = o2.offer_owner_id) AS u_o_min_price\n" +
            "         ON u_o_min_price.contract_id = c.id\n" +
            "ORDER  BY contract_id\n" +
            "LIMIT  :size ", nativeQuery = true)
    List<OfferReportDTO> getOfferReport(LocalDate startDate, LocalDate endDate,
                                        int startId, int size);
}
