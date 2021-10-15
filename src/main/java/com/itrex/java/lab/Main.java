package com.itrex.java.lab;

import com.itrex.java.lab.service.FlywayService;

public class Main {

    public static void main(String[] args) {

        FlywayService flywayService = new FlywayService();
        flywayService.migrate();


        while (true) {

        }

}
}
