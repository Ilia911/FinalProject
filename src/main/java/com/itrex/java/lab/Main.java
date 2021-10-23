package com.itrex.java.lab;

import com.itrex.java.lab.repository.impl.HibernateUserRepositoryImpl;
import com.itrex.java.lab.service.FlywayService;
import com.itrex.java.lab.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;


public class Main {

    public static void main(String[] args) {

        FlywayService flywayService = new FlywayService();
        flywayService.migrate();

        SessionFactory sessionFactory = HibernateUtil.INSTANCE.getSessionFactory();

        Session session = sessionFactory.openSession();
        HibernateUserRepositoryImpl repository = new HibernateUserRepositoryImpl(session);





        session.close();
    }
}
