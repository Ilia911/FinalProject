package com.itrex.java.lab;

import com.itrex.java.lab.config.ApplicationContextConfiguration;
import com.itrex.java.lab.exeption.ServiceException;
import com.itrex.java.lab.service.UserService;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
public class Main {

    public static void main(String[] args) throws ServiceException {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ApplicationContextConfiguration.class);

        UserService se = applicationContext.getBean(UserService.class);
        se.findAll();

        Arrays.stream(applicationContext.getBean(Environment.class).getActiveProfiles()).forEach(System.out::println);

        log.info("info log");
        log.error("error log");
    }
}
