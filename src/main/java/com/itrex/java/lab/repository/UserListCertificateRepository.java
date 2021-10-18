package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Certificate;
import java.util.List;
import java.util.Optional;

public interface UserListCertificateRepository {

    Optional<Certificate> assignCertificate(int userId, int certificateId);

    boolean removeCertificate(int userId, int certificateId);

    List<Certificate> findAllForUser(int userId);
}
