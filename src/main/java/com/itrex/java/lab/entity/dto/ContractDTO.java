package com.itrex.java.lab.entity.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractDTO {

    private int id;
    private UserDTO owner;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer startPrice;

}
