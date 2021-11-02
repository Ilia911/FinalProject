package com.itrex.java.lab;

import com.itrex.java.lab.config.ApplicationContextConfiguration;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.UserRepository;
import com.itrex.java.lab.repository.impl.JDBCUserRepositoryImpl;
import com.itrex.java.lab.service.FlywayService;
import com.itrex.java.lab.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {

    public static void main(String[] args) throws RepositoryException {

        FlywayService flywayService = new FlywayService();
        flywayService.migrate();

        SessionFactory sessionFactory = HibernateUtil.INSTANCE.getSessionFactory();

        Session session = sessionFactory.openSession();
//        HibernateUserRepositoryImpl repository = new HibernateUserRepositoryImpl(session);

        ApplicationContext ctx = new AnnotationConfigApplicationContext(ApplicationContextConfiguration.class);


        System.out.println(ctx.getBean(JDBCUserRepositoryImpl.class));
        UserRepository repository = ctx.getBean(UserRepository.class);
        repository.findAll().stream().forEach(System.out::println);


        session.close();
    }
}
