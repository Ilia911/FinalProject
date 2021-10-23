package com.itrex.java.lab.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public enum HibernateUtil {

    INSTANCE;

    private final SessionFactory sessionFactory;

    HibernateUtil() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
