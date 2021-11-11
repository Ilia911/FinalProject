package com.itrex.java.lab.service;

import com.itrex.java.lab.entity.dto.CertificateDTO;
import com.itrex.java.lab.exeption.ServiceException;
import java.util.List;

public interface CertificateService {

    List<CertificateDTO> findAllForUser(int userId) throws ServiceException;
}
