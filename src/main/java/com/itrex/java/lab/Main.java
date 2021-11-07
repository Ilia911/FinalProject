package com.itrex.java.lab;

import com.itrex.java.lab.config.ApplicationContextConfiguration;
import com.itrex.java.lab.entity.dto.UserDTO;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.service.OfferService;
import com.itrex.java.lab.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

    public static void main(String[] args) throws ServiceException {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationContextConfiguration.class);

        System.out.println(ctx.getBean(OfferService.class).find(1));

    }
}
