package com.itrex.java.lab.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OfferReportPageParameter {
    private final String firstStartContractDate;
    private final String lastStartContractDate;
    private final int startWithContractId;
    private final int size;
}
