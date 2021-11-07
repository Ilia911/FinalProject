package com.itrex.java.lab.service;

import com.itrex.java.lab.repository.ContractRepository;
import com.itrex.java.lab.repository.OfferRepository;
import com.itrex.java.lab.repository.RoleRepository;
import com.itrex.java.lab.repository.UserCertificateRepository;
import com.itrex.java.lab.repository.UserRepository;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan("com.itrex.java.lab.service")
public class TestServiceConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    @Primary
    public UserRepository hibernateUserRepositoryImpl() {
        return Mockito.mock(UserRepository.class);
    }

    @Bean
    @Primary
    public OfferRepository JDBCOfferRepositoryImpl() {
        return Mockito.mock(OfferRepository.class);
    }

    @Bean
    @Primary
    public RoleRepository hibernateRoleRepositoryImpl() {
        return Mockito.mock(RoleRepository.class);
    }

    @Bean
    @Primary
    public UserCertificateRepository hibernateUserCertificateRepositoryImpl() {
        return Mockito.mock(UserCertificateRepository.class);
    }

    @Bean
    @Primary
    public ContractRepository hibernateContractRepositoryImpl() {
        return Mockito.mock(ContractRepository.class);
    }
}
