package com.itrex.java.lab.service;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.ServiceException;
import java.util.List;

public interface CertificateService {

    List<Certificate> findAllForUser(int userId) throws ServiceException;
}
