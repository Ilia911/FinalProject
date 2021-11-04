package com.itrex.java.lab;

import com.itrex.java.lab.config.ApplicationContextConfiguration;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.UserRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

    public static void main(String[] args) throws RepositoryException {

        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationContextConfiguration.class);

        System.out.println(ctx.getBean("hibernateUserRepositoryImpl", UserRepository.class).findAll().size());
    }
}
