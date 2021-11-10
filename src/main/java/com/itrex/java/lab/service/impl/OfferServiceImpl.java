package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.dto.OfferDTO;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.OfferRepository;
import com.itrex.java.lab.service.OfferService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository repository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<OfferDTO> find(int id) throws ServiceException {
        OfferDTO offerDTO = null;
        try {
            Optional<Offer> offer = repository.find(id);
            if (offer.isPresent()) {
                offerDTO = convertOfferIntoDTO(offer.get());
            }
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return Optional.ofNullable(offerDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferDTO> findAll(int contractId) throws ServiceException {
        List<OfferDTO> offersDTO = new ArrayList<>();
        try {
            List<Offer> offers = repository.findAll(contractId);
            if (offers.size() > 0) {
                offersDTO = offers.stream().map(this::convertOfferIntoDTO).collect(Collectors.toList());
            }
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return offersDTO;
    }

    @Override
    @Transactional
    public boolean delete(int id) throws ServiceException {
        boolean result;
        try {
            result = repository.delete(id);
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return result;
    }

    @Override
    @Transactional
    public OfferDTO update(Offer offer) throws ServiceException {
        OfferDTO updatedOffer;
        try {
            updatedOffer = convertOfferIntoDTO(repository.update(offer));
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return updatedOffer;
    }

    @Override
    @Transactional
    public Optional<OfferDTO> add(Offer offer) throws ServiceException {

        OfferDTO newOfferDTO = null;
        try {
            Optional<Offer> newOffer = repository.add(offer);
            if (newOffer.isPresent()) {
                newOfferDTO = convertOfferIntoDTO(newOffer.get());
            }
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
        return Optional.ofNullable(newOfferDTO);
    }

    private OfferDTO convertOfferIntoDTO(Offer offer) {
        return modelMapper.map(offer, OfferDTO.class);
    }
}
