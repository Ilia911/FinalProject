package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.ContractRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateContractRepositoryImpl implements ContractRepository {

    private final Session session;
    private static final String FIND_CONTRACTS_QUERY = "select c from Contract c ";
    private static final String REMOVE_ALL_OFFERS_FOR_CONTRACT_QUERY
            = "DELETE FROM builder.offer where contract_id = ?";

    public HibernateContractRepositoryImpl(Session session) {
        this.session = session;
    }

    @Override
    public Optional<Contract> find(int id) throws RepositoryException {
        Contract contract;
        try {
            contract = session.find(Contract.class, id);
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return Optional.of(contract);
    }

    @Override
    public List<Contract> findAll() throws RepositoryException {
        List<Contract> contracts;
        try {
            contracts = session.createQuery(FIND_CONTRACTS_QUERY, Contract.class).list();
        } catch (Exception ex) {
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return contracts;
    }

    @Override
    public boolean delete(int id) throws RepositoryException {
        boolean result;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.createSQLQuery(REMOVE_ALL_OFFERS_FOR_CONTRACT_QUERY).setParameter(1, id).executeUpdate();
            Contract contract = session.find(Contract.class, id);
            if (contract != null) {
                session.delete(contract);
                result = (null == session.find(Contract.class, id));
            } else {
                result = false;
            }
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return result;
    }

    @Override
    public Contract update(Contract contract) throws RepositoryException {
        validateContractData(contract);

        Contract updatedContract;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update("Contract", contract);
            updatedContract = session.find(Contract.class, contract.getId());
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RepositoryException("Something was wrong in the repository", ex);
        }
        return updatedContract;
    }

    @Override
    public Optional<Contract> add(Contract contract) throws RepositoryException {
        validateContractData(contract);

        Contract createdContract;
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            int newContractId = (Integer) session.save("Contract", contract);
            createdContract = session.find(Contract.class, newContractId);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
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
        if (contract.getEndDate() == null || contract.getEndDate().isBefore(LocalDate.now())) {
            throw new RepositoryException("Contract field 'endDate' must not be null or before today " +
                    "(" + LocalDate.now() + ")!");
        }
        if (contract.getEndDate().isBefore(contract.getStartDate())) {
            throw new RepositoryException("Contract start date should be before end date!");
        }
    }
}
