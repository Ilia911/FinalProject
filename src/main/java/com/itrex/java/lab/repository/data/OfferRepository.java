package com.itrex.java.lab.repository.data;

import com.itrex.java.lab.entity.Offer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<Offer, Integer> {

    List<Offer> findAllByContractId(Integer contractId);

    List<Offer> findByOfferOwnerId(Integer id);
}
