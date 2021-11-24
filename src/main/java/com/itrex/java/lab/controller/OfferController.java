package com.itrex.java.lab.controller;

import com.itrex.java.lab.entity.dto.OfferDTO;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.service.OfferService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OfferController {

    private final OfferService service;

    @GetMapping("/contracts/offers/{contractId}")
    public ResponseEntity<List<OfferDTO>> findAllForGivenContract(@PathVariable(name = "contractId") int contractId) throws ServiceException {

        List<OfferDTO> offers = service.findAll(contractId);

        return offers != null && !offers.isEmpty()
                ? new ResponseEntity<>(offers, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/offers/{id}")
    public ResponseEntity<OfferDTO> find(@PathVariable(name = "id") int id) throws ServiceException {

        Optional<OfferDTO> offerDTO = service.find(id);

        return offerDTO.isPresent()
                ? new ResponseEntity<>(offerDTO.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/offers/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable(name = "id") int id) throws ServiceException {

        boolean result = service.delete(id);

        return result
                ? new ResponseEntity<>(result, HttpStatus.OK)
                : new ResponseEntity<>(result, HttpStatus.NOT_MODIFIED);
    }

    @PutMapping("/offers/update")
    public ResponseEntity<OfferDTO> update(@RequestBody OfferDTO offer) throws ServiceException {

        OfferDTO updatedOffer = service.update(offer);

        return updatedOffer != null
                ? new ResponseEntity<>(updatedOffer, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping("/offer/new")
    public ResponseEntity<OfferDTO> add(@RequestBody OfferDTO offer) throws ServiceException {

        Optional<OfferDTO> newOffer = service.add(offer);

        return newOffer.isPresent()
                ? new ResponseEntity<>(newOffer.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }
}
