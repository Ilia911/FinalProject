package com.itrex.java.lab;

import com.itrex.java.lab.repository.impl.JDBCUserRepositoryImpl;
import com.itrex.java.lab.service.FlywayService;
import org.h2.jdbcx.JdbcConnectionPool;

import static com.itrex.java.lab.properties.H2Properties.H2_PASSWORD;
import static com.itrex.java.lab.properties.H2Properties.H2_URL;
import static com.itrex.java.lab.properties.H2Properties.H2_USER;


public class Main {

    public static void main(String[] args) {

        FlywayService flywayService = new FlywayService();
        flywayService.migrate();

        JdbcConnectionPool connectionPool = JdbcConnectionPool.create(H2_URL, H2_USER, H2_PASSWORD);

        final JDBCUserRepositoryImpl userRepository = new JDBCUserRepositoryImpl(connectionPool);

/*        final List<User> userList = userRepository.findAll();

        System.out.println("Before execution task");
        System.out.println(userList);
        System.out.println("After execution");*/

        connectionPool.dispose();


    }
}
