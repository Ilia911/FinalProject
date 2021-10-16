package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Offer;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {

    Optional<Offer> find(int id);

    List<Offer> findAll(int contractId);

    boolean delete(int id);

    Offer update(Offer offer);

    Optional<Offer> add(Offer offer);
}
