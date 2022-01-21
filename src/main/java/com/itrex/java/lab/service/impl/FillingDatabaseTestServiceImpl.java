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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class FillingDatabaseTestServiceImpl implements FillingDatabaseTestService {

    private static int userNumber = 1;
    private static final String BASE_CUSTOMER_NAME = "customer_generated";
    private static final String BASE_CONTRACTOR_NAME = "contractor_generated";
    private static final String PASSWORD = "$2a$12$ma0C6gCWmZL/d3FSt78mF.FCcLdfBqc4XXyRrDBv54EbWEPcj3OIC";
    private static final String EMAIL_FORMAT = "%s%s@gmail.com";
    private static final String DESCRIPTION_FORMAT = "Contract%s";
    private static final int MIN_CONTRACT_AMOUNT = 5;
    private static final int MAX_CONTRACT_AMOUNT = 20;
    private static final int MIN_OFFER_AMOUNT = 5;
    private static final int MAX_OFFER_AMOUNT = 15;
    private static final int CUSTOMER_AMOUNT = 50;
    private static final int CONTRACTOR_AMOUNT = 200;
    private static final int RANDOM_DAYS_AMOUNT = 90;
    private static final int CONTRACT_DURATION_AMOUNT_IN_DAYS = 100;
    private static final int MIN_PRICE = 20000;
    private static final int ADDITIONAL_MAX_RANDOM_PRICE = 10000;
    private static final int RANDOM_DISCOUNT = 5000;

    private static final Random RANDOM_GENERATOR = new Random();

    private final UserRepository userRepository;
    private final ContractRepository contractRepository;
    private final OfferRepository offerRepository;

    @Override
    public void fillTestDatabase() {
        fillUserTable(Role.CUSTOMER.toString(), CUSTOMER_AMOUNT);
        fillUserTable(Role.CONTRACTOR.toString(), CONTRACTOR_AMOUNT);
        fillContractTableForEachCustomer(MIN_CONTRACT_AMOUNT, MAX_CONTRACT_AMOUNT);
        fillOffersForEachContract(MIN_OFFER_AMOUNT, MAX_OFFER_AMOUNT);
    }

    private void fillUserTable(String role, int quantity) {
        if (Role.CUSTOMER.name().equals(role)) {
            List<User> userList = new ArrayList<>();
            for (int i = 0; i < quantity; i++) {
                User user = User.builder()
                        .name(BASE_CUSTOMER_NAME + userNumber)
                        .password(PASSWORD)
                        .role(Role.CUSTOMER)
                        .status(Status.ACTIVE)
                        .email(String.format(EMAIL_FORMAT, BASE_CUSTOMER_NAME, userNumber))
                        .build();
                userList.add(user);
                userNumber++;
            }
            userRepository.saveAll(userList);
        }

        if (Role.CONTRACTOR.name().equals(role)) {
            List<User> userList = new ArrayList<>();
            for (int i = 0; i < quantity; i++) {
                User user = User.builder()
                        .name(BASE_CONTRACTOR_NAME + userNumber)
                        .password(PASSWORD)
                        .role(Role.CONTRACTOR)
                        .status(Status.ACTIVE)
                        .email(String.format(EMAIL_FORMAT, BASE_CONTRACTOR_NAME, userNumber))
                        .build();
                userList.add(user);
                userNumber++;
            }
            userRepository.saveAll(userList);
        }
    }

    private void fillContractTableForEachCustomer(int minContractAmount, int maxContractAmount) {
        List<Contract> contracts = new ArrayList<>();
        Iterable<User> contractOwners = userRepository.findByRole(Role.CUSTOMER);

        for (User user : contractOwners) {
            List<Contract> tempContracts = createContracts(user, minContractAmount, maxContractAmount);
            contracts.addAll(tempContracts);
        }
        contractRepository.saveAll(contracts);
    }

    private List<Contract> createContracts(User user, int minContractAmount, int maxContractAmount) {
        List<Contract> contracts = new ArrayList<>();
        int contractAmount = RANDOM_GENERATOR.nextInt(maxContractAmount - minContractAmount) + minContractAmount;
        for (int i = 0; i < contractAmount; i++) {
            Contract contract = Contract.builder()
                    .owner(user)
                    .description(String.format(DESCRIPTION_FORMAT, i))
                    .startDate(LocalDate.now().plusDays(RANDOM_GENERATOR.nextInt(RANDOM_DAYS_AMOUNT)))
                    .endDate(LocalDate.now().plusDays(CONTRACT_DURATION_AMOUNT_IN_DAYS)
                            .plusDays(RANDOM_GENERATOR.nextInt(RANDOM_DAYS_AMOUNT)))
                    .startPrice(RANDOM_GENERATOR.nextInt(ADDITIONAL_MAX_RANDOM_PRICE) + MIN_PRICE)
                    .build();
            contracts.add(contract);
        }
        return contracts;
    }

    private void fillOffersForEachContract(int minOfferAmount, int maxOfferAmount) {
        Iterable<Contract> contracts = contractRepository.findAll();
        Iterable<User> allContractors = userRepository.findByRole(Role.CONTRACTOR);
        List<User> contractors = new ArrayList<>();
        allContractors.forEach(contractors::add);

        for (Contract contract : contracts) {
            List<Offer> offers = createOffers(contract, minOfferAmount, maxOfferAmount, contractors);
            offerRepository.saveAll(offers);
        }
    }

    private List<Offer> createOffers(Contract contract, int minOfferAmount, int maxOfferAmount, List<User> contractors) {
        List<Offer> offers = new ArrayList<>();
        int offerAmount = RANDOM_GENERATOR.nextInt(maxOfferAmount - minOfferAmount) + minOfferAmount;

        for (int i = 0; i < offerAmount; i++) {
            Offer offer = Offer.builder()
                    .offerOwner(contractors.get(RANDOM_GENERATOR.nextInt(contractors.size())))
                    .contract(contract)
                    .price(contract.getStartPrice() - RANDOM_GENERATOR.nextInt(RANDOM_DISCOUNT))
                    .build();
            offers.add(offer);
        }
        return offers;
    }
}
