package com.itrex.java.lab.service;

import com.itrex.java.lab.repository.CertificateRepository;
import com.itrex.java.lab.repository.ContractRepository;
import com.itrex.java.lab.repository.OfferRepository;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.UserRepository;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.itrex.java.lab.service")
public class TestServiceConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public UserRepository hibernateUserRepositoryImpl() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    public OfferRepository JDBCOfferRepositoryImpl() {
        return Mockito.mock(OfferRepository.class);
    }

    @Bean
    public RoleRepository hibernateRoleRepositoryImpl() {
        return Mockito.mock(RoleRepository.class);
    }

    @Bean
    public CertificateRepository hibernateUserCertificateRepositoryImpl() {
        return Mockito.mock(CertificateRepository.class);
    }

    @Bean
    public ContractRepository hibernateContractRepositoryImpl() {
        return Mockito.mock(ContractRepository.class);
    }
}
