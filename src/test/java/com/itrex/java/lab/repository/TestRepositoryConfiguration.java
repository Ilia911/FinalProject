package com.itrex.java.lab.repository;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcConnectionPool;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan("com.itrex.java.lab.repository")
@PropertySource("classpath:/application.properties")
public class TestRepositoryConfiguration {

    @Autowired
    Environment env;

    @Bean(initMethod = "migrate")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Flyway flyway() {
        return Flyway.configure()
                .dataSource(env.getProperty("database.url"),
                        env.getProperty("database.login"),
                        env.getProperty("database.password"))
                .locations(env.getProperty("database.migration.location"))
                .load();
    }

    @Bean
    @DependsOn("flyway")
    public JdbcConnectionPool jdbcConnectionPool() {
        return JdbcConnectionPool.create(env.getProperty("database.url"),
                env.getProperty("database.login"),
                env.getProperty("database.password"));
    }

    @Bean
    @DependsOn("flyway")
    public SessionFactory sessionFactory() {
        return new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
    }

    @Bean
    @Scope
    public Session session() {
        return sessionFactory().openSession();
    }
}
