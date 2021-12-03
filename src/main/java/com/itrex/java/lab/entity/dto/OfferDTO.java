package com.itrex.java.lab.entity.dto;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferDTO {

    private int id;
    private int offerOwnerId;
    private int contractId;
    private Integer price;

    public static Offer convertIntoOffer(OfferDTO offerDTO) {
        return Offer.builder()
                .id(offerDTO.getId())
                .offerOwner(User.builder().id(offerDTO.getOfferOwnerId()).build())
                .contract(Contract.builder().id(offerDTO.getContractId()).build())
                .price(offerDTO.getPrice())
                .build();
    }

    public static OfferDTO convertFromOffer(Offer offer) {
        return OfferDTO.builder()
                .id(offer.getId())
                .offerOwnerId(offer.getOfferOwner().getId())
                .contractId(offer.getContract().getId())
                .price(offer.getPrice())
                .build();
    }
}
