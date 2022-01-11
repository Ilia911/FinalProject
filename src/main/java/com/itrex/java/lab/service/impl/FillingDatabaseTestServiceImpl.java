package com.itrex.java.lab.service.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.Status;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.repository.data.ContractRepository;
import com.itrex.java.lab.repository.data.OfferRepository;
import com.itrex.java.lab.repository.data.UserRepository;
import com.itrex.java.lab.service.FillingDatabaseTestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class FillingDatabaseTestServiceImpl implements FillingDatabaseTestService {

    private static int userNumber = 1;
    private static final String BASE_CUSTOMER_NAME = "customer_generated";
    private static final String BASE_CONTRACTOR_NAME = "contractor_generated";
    private static final int MIN_CONTRACT_AMOUNT = 5;
    private static final int MAX_CONTRACT_AMOUNT = 20;
    private static final int MIN_OFFER_AMOUNT = 5;
    private static final int MAX_OFFER_AMOUNT = 15;

    private static final Random RANDOM_GENERATOR = new Random();

    private final UserRepository userRepository;
    private final ContractRepository contractRepository;
    private final OfferRepository offerRepository;

    @Override
    public boolean fillUserTable(String role, int quantity) {
        if (Role.CUSTOMER.name().equals(role)) {
            List<User> userList = new ArrayList<>();
            for (int i = 0; i < quantity; i++) {
                User user = User.builder()
                        .name(BASE_CUSTOMER_NAME + userNumber)
                        .password("$2a$12$ma0C6gCWmZL/d3FSt78mF.FCcLdfBqc4XXyRrDBv54EbWEPcj3OIC")
                        .role(Role.CUSTOMER)
                        .status(Status.ACTIVE)
                        .email(BASE_CUSTOMER_NAME + userNumber + "@gmail.com")
                        .build();
                userList.add(user);
                userNumber++;
            }
            userRepository.saveAll(userList);
            return true;
        }

        if (Role.CONTRACTOR.name().equals(role)) {
            List<User> userList = new ArrayList<>();
            for (int i = 0; i < quantity; i++) {
                User user = User.builder()
                        .name(BASE_CONTRACTOR_NAME + userNumber)
                        .password("$2a$12$ma0C6gCWmZL/d3FSt78mF.FCcLdfBqc4XXyRrDBv54EbWEPcj3OIC")
                        .role(Role.CONTRACTOR)
                        .status(Status.ACTIVE)
                        .email(BASE_CONTRACTOR_NAME + userNumber + "@gmail.com")
                        .build();
                userList.add(user);
                userNumber++;
            }
            userRepository.saveAll(userList);
            return true;
        }
        return false;
    }

    @Override
    public boolean fillContractTableForEachCustomer(int minContractAmount, int maxContractAmount) {
        List<Contract> contracts = new ArrayList<>();
        Iterable<User> contractOwners = userRepository.findByRole(Role.CUSTOMER);

        for (User user : contractOwners) {
            contracts.addAll(createContracts(user, minContractAmount, maxContractAmount));
        }
        contractRepository.saveAll(contracts);
        return true;
    }

    private List<Contract> createContracts(User user, int minContractAmount, int maxContractAmount) {
        List<Contract> contracts = new ArrayList<>();
        int contractAmount = RANDOM_GENERATOR.nextInt(maxContractAmount - minContractAmount) + minContractAmount;
        for (int i = 0; i < contractAmount; i++) {
            Contract contract = Contract.builder()
                    .owner(user)
                    .description("Contract" + i)
                    .startDate(LocalDate.now().plusDays(RANDOM_GENERATOR.nextInt(90)))
                    .endDate(LocalDate.now().plusDays(100).plusDays(RANDOM_GENERATOR.nextInt(90)))
                    .startPrice(RANDOM_GENERATOR.nextInt(10000) + 20000)
                    .build();
            contracts.add(contract);
        }
        return contracts;
    }

    @Override
    public boolean fillOffersForEachContract(int minOfferAmount, int maxOfferAmount) {
        Iterable<Contract> contracts = contractRepository.findAll();
        Iterable<User> allContractors = userRepository.findByRole(Role.CONTRACTOR);
        List<User> contractors = new ArrayList<>();
        allContractors.forEach(contractors::add);

        for (Contract contract : contracts) {
            List<Offer> offers = new ArrayList<>();
            offers.addAll(createOffers(contract, minOfferAmount, maxOfferAmount, contractors));
            offerRepository.saveAll(offers);
        }
        return true;
    }

    private List<Offer> createOffers(Contract contract, int minOfferAmount, int maxOfferAmount, List<User> contractors) {
        List<Offer> offers = new ArrayList<>();
        int offerAmount = RANDOM_GENERATOR.nextInt(maxOfferAmount - minOfferAmount) + minOfferAmount;

        for (int i = 0; i < offerAmount; i++) {
            Offer offer = Offer.builder()
                    .offerOwner(contractors.get(RANDOM_GENERATOR.nextInt(contractors.size())))
                    .contract(contract)
                    .price(contract.getStartPrice() - RANDOM_GENERATOR.nextInt(5000))
                    .build();
            offers.add(offer);
        }
        return offers;
    }
}
