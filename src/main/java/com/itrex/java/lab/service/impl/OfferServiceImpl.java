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
        try {
            OfferDTO offerDTO = null;
            Optional<Offer> offer = repository.find(id);
            if (offer.isPresent()) {
                offerDTO = convertOfferIntoDTO(offer.get());
            }
            return Optional.ofNullable(offerDTO);
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferDTO> findAll(int contractId) throws ServiceException {
        try {
            List<OfferDTO> offersDTO = new ArrayList<>();
            List<Offer> offers = repository.findAll(contractId);
            if (offers.size() > 0) {
                offers.forEach(offer -> offersDTO.add(convertOfferIntoDTO(offer)));
            }
            return offersDTO;
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) throws ServiceException {
        try {
            return repository.delete(id);
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public OfferDTO update(OfferDTO offerDTO) throws ServiceException {
        try {
            Optional<Offer> optionalOffer = repository.find(offerDTO.getId());
            if (optionalOffer.isPresent()) {
                if (offerDTO.getPrice() != null && offerDTO.getPrice() > 0) {
                    optionalOffer.get().setPrice(offerDTO.getPrice());
                    return convertOfferIntoDTO(repository.update(optionalOffer.get()));
                } else {
                    throw new ServiceException("Offer price should be positive!");
                }
            }
            throw new ServiceException(String.format("Offer with %s id does not exist", offerDTO.getId()));
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    @Transactional
    public Optional<OfferDTO> add(OfferDTO offerDTO) throws ServiceException {

        OfferDTO newOfferDTO = null;
        try {
            Offer offer = convertOfferDTOIntoOffer(offerDTO);
            Optional<Offer> newOffer = repository.add(offer);
            if (newOffer.isPresent()) {
                newOfferDTO = convertOfferIntoDTO(newOffer.get());
            }
            return Optional.ofNullable(newOfferDTO);
        } catch (RepositoryException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    private OfferDTO convertOfferIntoDTO(Offer offer) {
        return modelMapper.map(offer, OfferDTO.class);
    }

    private Offer convertOfferDTOIntoOffer(OfferDTO offerDTO) {
        return modelMapper.map(offerDTO, Offer.class);
    }
}
