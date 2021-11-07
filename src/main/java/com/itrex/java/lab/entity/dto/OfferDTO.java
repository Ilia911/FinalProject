package com.itrex.java.lab.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OfferDTO {

    private int id;
    private UserDTO offerOwner;
    private ContractDTO contract;
    private Integer price;

    public OfferDTO(int id, UserDTO offerOwner, ContractDTO contract, Integer price) {
        this.id = id;
        this.offerOwner = offerOwner;
        this.contract = contract;
        this.price = price;
    }
}
