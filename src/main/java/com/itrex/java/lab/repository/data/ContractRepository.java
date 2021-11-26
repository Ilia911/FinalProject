package com.itrex.java.lab.repository.data;

import com.itrex.java.lab.entity.Contract;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

    List<Contract> findAllByOwnerId(Integer id);
}
