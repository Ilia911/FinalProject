package com.itrex.java.lab.repository.data;

import com.itrex.java.lab.entity.Certificate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

    @Query("SELECT c FROM User u JOIN u.certificates c  WHERE u.id =:userId")
    List<Certificate> findAllByUserId(Integer userId);
}
