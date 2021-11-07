package com.itrex.java.lab.entity.dto;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContractDTO {

    private int id;
    private UserDTO owner;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer startPrice;

    public ContractDTO(int id, UserDTO owner, String description, LocalDate startDate, LocalDate endDate, Integer startPrice) {
        this.id = id;
        this.owner = owner;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startPrice = startPrice;
    }
}
