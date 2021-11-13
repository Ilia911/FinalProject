package com.itrex.java.lab;

import com.itrex.java.lab.config.ApplicationContextConfiguration;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.service.CertificateService;
import com.itrex.java.lab.service.ContractService;
import com.itrex.java.lab.service.OfferService;
import com.itrex.java.lab.service.UserService;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) throws ServiceException {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContextConfiguration.class);
        System.out.println("\n\n\n");

        context.getBean(ContractService.class).findAll();
        context.getBean(OfferService.class).find(1);
        context.getBean(CertificateService.class).findAllForUser(3);
        context.getBean(UserService.class).findAll();
    }
}
