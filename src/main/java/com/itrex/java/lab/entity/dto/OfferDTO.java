package com.itrex.java.lab.entity.dto;

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
    private UserDTO offerOwner;
    private ContractDTO contract;
    private Integer price;

}
