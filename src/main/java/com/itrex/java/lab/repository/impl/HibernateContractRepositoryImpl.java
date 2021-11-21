package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.ContractRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
@Primary
public class HibernateContractRepositoryImpl implements ContractRepository {

    private static final String FIND_CONTRACTS_QUERY = "select c from Contract c ";
    private static final String FIND_CONTRACTS_BY_USER_ID_QUERY = "select c from Contract c where c.owner.id =:userId";

    private EntityManager entityManager;

    @Override
    public Optional<Contract> find(int id) throws RepositoryException {
        Contract contract;
        try {
            contract = entityManager.find(Contract.class, id);
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return Optional.of(contract);
    }

    @Override
    public List<Contract> findAll() throws RepositoryException {
        List<Contract> contracts;
        try {
            contracts = entityManager.createQuery(FIND_CONTRACTS_QUERY, Contract.class).getResultList();
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return contracts;
    }

    @Override
    public List<Contract> findAllByUserId(int userId) throws RepositoryException {
        List<Contract> contracts;
        try {
            contracts = entityManager.createQuery(FIND_CONTRACTS_BY_USER_ID_QUERY, Contract.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return contracts;
    }

    @Override
    public boolean delete(int id) throws RepositoryException {
        boolean result;
        try {
            Contract contract = entityManager.find(Contract.class, id);
            if (contract != null) {
                entityManager.remove(contract);
                result = (null == entityManager.find(Contract.class, id));
            } else {
                result = false;
            }
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return result;
    }

    @Override
    public Contract update(Contract contract) throws RepositoryException {
        validateContractData(contract);

        Contract updatedContract;
        try {
            updatedContract = entityManager.merge(contract);
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return updatedContract;
    }

    @Override
    public Optional<Contract> add(Contract contract) throws RepositoryException {
        validateContractData(contract);

        Contract createdContract;
        try {
            Session session = entityManager.unwrap(Session.class);
            int newContractId = (Integer) session.save("Contract", contract);
            createdContract = session.find(Contract.class, newContractId);
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return Optional.ofNullable(createdContract);
    }

    private void validateContractData(Contract contract) throws RepositoryException {
        if (contract == null) {
            throw new RepositoryException("Contract can not be 'null'!");
        }
        if (contract.getStartDate() == null || contract.getStartDate().isBefore(LocalDate.now())) {
            throw new RepositoryException("Contract field 'startDate' must not be null or before today " +
                    "(" + LocalDate.now() + ")!");
        }
        if (contract.getEndDate() == null || contract.getEndDate().isBefore(contract.getStartDate())) {
            throw new RepositoryException("Contract field 'endDate' must not be null or before today " +
                    "(" + LocalDate.now() + ")!");
        }
    }
}
