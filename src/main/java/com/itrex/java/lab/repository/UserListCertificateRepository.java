package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.RepositoryException;
import java.util.List;
import java.util.Optional;

public interface UserListCertificateRepository {

    Optional<Certificate> assignCertificate(int userId, int certificateId) throws RepositoryException;

    boolean removeCertificate(int userId, int certificateId) throws RepositoryException;

    List<Certificate> findAllForUser(int userId) throws RepositoryException;
}
