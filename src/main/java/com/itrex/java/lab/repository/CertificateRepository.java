package com.itrex.java.lab.repository;

import com.itrex.java.lab.entity.Certificate;
import com.itrex.java.lab.exeption.RepositoryException;
import java.util.List;
import java.util.Optional;

public interface CertificateRepository {

    List<Certificate> findAllForUser(int userId) throws RepositoryException;

    Optional<Certificate> findById(int id) throws RepositoryException;
}
