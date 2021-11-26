package com.itrex.java.lab.service;

import com.itrex.java.lab.entity.dto.CertificateDTO;
import java.util.List;

public interface CertificateService {

    List<CertificateDTO> findAllForUser(int userId);
}
