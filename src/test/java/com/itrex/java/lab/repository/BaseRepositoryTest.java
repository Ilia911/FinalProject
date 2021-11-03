package com.itrex.java.lab.repository;

import org.flywaydb.core.Flyway;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseRepositoryTest {

    @Autowired
    private Flyway flyway;
    @Autowired
    private Session session;

    @AfterEach
    public void clean() {
        flyway.clean();
        session.clear();
    }
}
