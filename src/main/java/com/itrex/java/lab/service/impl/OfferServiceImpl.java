package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.dto.OfferDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.data.OfferRepository;
import com.itrex.java.lab.service.OfferService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<OfferDTO> find(int id) {

        OfferDTO offerDTO = null;
        Optional<Offer> offer = repository.findById(id);
        if (offer.isPresent()) {
            offerDTO = OfferDTO.convertFromOffer(offer.get());
        }
        return Optional.ofNullable(offerDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OfferDTO> findAll(int contractId) {

        List<OfferDTO> offersDTO = new ArrayList<>();
        List<Offer> offers = repository.findAllByContractId(contractId);
        if (offers.size() > 0) {
            offers.forEach(offer -> offersDTO.add(OfferDTO.convertFromOffer(offer)));
        }
        return offersDTO;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        repository.deleteById(id);
        return repository.findById(id).isEmpty();
    }

    @Override
    @Transactional
    public OfferDTO update(OfferDTO offerDTO) throws ServiceException {

        Optional<Offer> optionalOffer = repository.findById(offerDTO.getId());
        if (optionalOffer.isPresent()) {
            if (offerDTO.getPrice() != null && offerDTO.getPrice() > 0) {
                optionalOffer.get().setPrice(offerDTO.getPrice());
                repository.flush();
                return OfferDTO.convertFromOffer(repository.findById(offerDTO.getId()).get());
            } else {
                throw new ServiceException("Offer price should be positive!");
            }
        }
        throw new ServiceException(String.format("Offer with %s id does not exist", offerDTO.getId()));
    }

    @Override
    @Transactional
    public Optional<OfferDTO> add(OfferDTO offerDTO) {

        OfferDTO newOfferDTO;
        Offer offer = OfferDTO.convertIntoOffer(offerDTO);
        Offer newOffer = repository.save(offer);
        newOfferDTO = OfferDTO.convertFromOffer(newOffer);
        return Optional.ofNullable(newOfferDTO);
    }
}
