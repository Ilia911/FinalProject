package com.itrex.java.lab.listener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:/application.properties")
@Slf4j
public class LoggerContextInitializer implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private Environment environment;

    @Value("${logging.config.dev}")
    private String log4jDevPropertiesPath;
    @Value("${logging.config.prod}")
    private String log4jProdPropertiesPath;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Properties properties = new Properties();

        if (Arrays.asList(environment.getActiveProfiles()).contains("dev")) {
            try {
                properties.load(new FileInputStream(log4jDevPropertiesPath));
                PropertyConfigurator.configure(properties);
                log.info("dev_log4j.properties file successfully loaded!");
            } catch (IOException e) {
                log.error("dev_log4j.properties file did not load!");
            }
        }

        if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
            try {
                properties.load(new FileInputStream(log4jProdPropertiesPath));
                PropertyConfigurator.configure(properties);
                log.info("prod_log4j.properties file successfully loaded!");
            } catch (IOException e) {
                log.error("prod_log4j.properties file did not load!");
            }
        } else {
            log.warn("No one available logging.config property (that contains path to log4j.properties file) was found!");
        }
    }
}
