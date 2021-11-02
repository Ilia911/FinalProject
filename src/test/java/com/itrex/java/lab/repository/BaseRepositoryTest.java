package com.itrex.java.lab.repository;

import com.itrex.java.lab.config.ApplicationContextConfiguration;
import com.itrex.java.lab.service.FlywayService;
import org.junit.jupiter.api.AfterEach;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public abstract class BaseRepositoryTest {

    private final FlywayService flywayService;
    private final ApplicationContext applicationContext;

    public BaseRepositoryTest() {
        applicationContext = new AnnotationConfigApplicationContext(ApplicationContextConfiguration.class);

        flywayService = applicationContext.getBean(FlywayService.class);
    }

    // todo delete after review
//    @BeforeEach
//    public void initDB() {
//        flywayService.migrate();
//    }

    @AfterEach
    public void cleanDB() {
        flywayService.clean();
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
