package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.exeption.RepositoryException;
import java.util.List;
import java.util.Optional;

public interface JDBCOfferRepository {

    Optional<Offer> find(int id) throws RepositoryException;

    List<Offer> findAll(int contractId) throws RepositoryException;

    boolean delete(int id) throws RepositoryException;

    Offer update(Offer offer) throws RepositoryException;

    Optional<Offer> add(Offer offer) throws RepositoryException;
}
