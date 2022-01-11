package com.itrex.java.lab.service;

public interface FillingDatabaseTestService {

    boolean fillUserTable(String role, int amountOfGeneratedUsers);
    boolean fillContractTableForEachCustomer(int minContractAmount, int maxContractAmount);
    boolean fillOffersForEachContract(int minOfferAmount, int maxOfferAmount);
}
