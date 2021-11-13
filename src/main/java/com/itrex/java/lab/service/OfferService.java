package com.itrex.java.lab.service;

import com.itrex.java.lab.entity.dto.OfferDTO;
import com.itrex.java.lab.exeption.ServiceException;
import java.util.List;
import java.util.Optional;

public interface OfferService {

    Optional<OfferDTO> find(int id) throws ServiceException;

    List<OfferDTO> findAll(int contractId) throws ServiceException;

    boolean delete(int id) throws ServiceException;

    OfferDTO update(OfferDTO offer) throws ServiceException;

    Optional<OfferDTO> add(OfferDTO offer) throws ServiceException;
}
