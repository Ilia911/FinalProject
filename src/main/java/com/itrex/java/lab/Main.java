package com.itrex.java.lab;

import com.itrex.java.lab.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class Main implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        UserService se = context.getBean(UserService.class);
        Environment env = context.getBean(Environment.class);

        for (String activeProfile : env.getActiveProfiles()) {
            System.out.println(activeProfile);
        }

        se.delete(1);
        se.findAll();
    }
}
