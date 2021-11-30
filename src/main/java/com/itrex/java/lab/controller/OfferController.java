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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService service;

    @GetMapping("/contracts/{id}")
    public ResponseEntity<List<OfferDTO>> findAllForGivenContract(@PathVariable(name = "id") int id) {

        List<OfferDTO> offers = service.findAll(id);

        return offers != null && !offers.isEmpty()
                ? new ResponseEntity<>(offers, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> find(@PathVariable(name = "id") int id) throws ServiceException {

        Optional<OfferDTO> offerDTO = service.find(id);

        return offerDTO.map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") int id) {

        boolean result = service.delete(id);

        return result
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> update(@PathVariable(name = "id") int id, @RequestBody OfferDTO offer) throws ServiceException {

        OfferDTO updatedOffer = service.update(offer);

        return updatedOffer != null
                ? new ResponseEntity<>(updatedOffer, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @PostMapping
    public ResponseEntity<OfferDTO> add(@RequestBody OfferDTO offer) {

        Optional<OfferDTO> newOffer = service.add(offer);

        return newOffer.map(offerDTO -> new ResponseEntity<>(offerDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE));
    }
}
