package com.itrex.java.lab.entity.dto;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.User;
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
    private int ownerId;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer startPrice;

    public static Contract convertToContract(ContractDTO contractDTO) {
        return Contract.builder()
                .id(contractDTO.getId())
                .owner(User.builder().id(contractDTO.getOwnerId()).build())
                .description(contractDTO.getDescription())
                .startDate(contractDTO.getStartDate())
                .endDate(contractDTO.getEndDate())
                .startPrice(contractDTO.getStartPrice())
                .build();
    }

    public static ContractDTO convertFromContract(Contract contract) {
        return ContractDTO.builder()
                .id(contract.getId())
                .ownerId(contract.getOwner().getId())
                .description(contract.getDescription())
                .startDate(contract.getStartDate())
                .endDate(contract.getEndDate())
                .startPrice(contract.getStartPrice())
                .build();
    }
}
