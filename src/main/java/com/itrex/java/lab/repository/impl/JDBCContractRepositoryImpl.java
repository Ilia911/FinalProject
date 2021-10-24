package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.ContractRepository;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

public class JDBCContractRepositoryImpl implements ContractRepository {

    private final DataSource dataSource;

    private static final String ID_COLUMN = "id";
    private static final String OWNER_ID_COLUMN = "owner_id";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String START_DATE_COLUMN = "start_date";
    private static final String END_DATE_COLUMN = "end_date";
    private static final String START_PRICE_COLUMN = "start_price";
    private static final String FIND_CONTRACT_BY_ID_QUERY = "SELECT * FROM builder.contract where id = ?";
    private static final String FIND_ALL_CONTRACTS_QUERY
            = "SELECT * FROM builder.contract";
    private static final String DELETE_CONTRACT_QUERY
            = "DELETE FROM builder.contract where id = ?;";
    private static final String UPDATE_CONTRACT_QUERY
            = "UPDATE builder.contract SET description = ?, start_date = ?, end_date = ?, start_price = ? WHERE id = ?";
    private static final String ADD_CONTRACT_QUERY
            = "INSERT INTO builder.contract(owner_id, description, start_date, end_date, start_price) VALUES (?, ?, ?, ?, ?)";
    private static final String REMOVE_ALL_OFFERS_FOR_CONTRACT_QUERY = "DELETE FROM builder.offer where contract_id = ?";

    public JDBCContractRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Contract> find(int id) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            return Optional.ofNullable(findContractById(conn, id));
        } catch (SQLException ex) {
            throw new RepositoryException("Can't find Contracts", ex);
        }
    }

    @Override
    public List<Contract> findAll() throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            List<Contract> resultList = new ArrayList<>();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_CONTRACTS_QUERY);

            while (resultSet.next()) {
                Contract contract = createContract(resultSet);
                resultList.add(contract);
            }
            return resultList;
        } catch (SQLException ex) {
            throw new RepositoryException("Can't find Contracts", ex);
        }
    }

    @Override
    public boolean delete(int id) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            boolean result;
            try {
                removeAllOffersForContract(conn, id);
                PreparedStatement preparedStatement = conn.prepareStatement(DELETE_CONTRACT_QUERY);
                preparedStatement.setInt(1, id);
                result = preparedStatement.executeUpdate() == 1;
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw new RepositoryException("Can't delete Contracts", ex);
            } finally {
                conn.setAutoCommit(true);
            }
            return result;
        } catch (SQLException ex) {
            throw new RepositoryException("Can't delete Contracts", ex);
        }
    }

    @Override
    public Contract update(Contract contract) throws RepositoryException {

        validateContractData(contract);

        try (Connection conn = dataSource.getConnection()) {
            Contract updatedContract;
            conn.setAutoCommit(false);
            try {
                PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_CONTRACT_QUERY);
                preparedStatement.setString(1, contract.getDescription());
                preparedStatement.setDate(2, Date.valueOf(contract.getStartDate().toString()));
                preparedStatement.setDate(3, Date.valueOf(contract.getEndDate().toString()));
                preparedStatement.setInt(4, contract.getStartPrice());
                preparedStatement.setInt(5, contract.getId());
                preparedStatement.execute();
                updatedContract = findContractById(conn, contract.getId());
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw new RepositoryException("Can't update Contracts", ex);
            } finally {
                conn.setAutoCommit(true);
            }
            return updatedContract;
        } catch (SQLException ex) {
            throw new RepositoryException("Can't update Contracts", ex);
        }
    }

    @Override
    public Optional<Contract> add(Contract contract) throws RepositoryException {

        validateContractData(contract);

        try (Connection conn = dataSource.getConnection()) {
            Contract insertedContract = null;
            conn.setAutoCommit(false);
            try {
                PreparedStatement preparedSt = conn.prepareStatement(ADD_CONTRACT_QUERY, Statement.RETURN_GENERATED_KEYS);
                preparedSt.setInt(1, contract.getOwnerId());
                preparedSt.setString(2, contract.getDescription());
                preparedSt.setDate(3, Date.valueOf(contract.getStartDate().toString()));
                preparedSt.setDate(4, Date.valueOf(contract.getEndDate().toString()));
                preparedSt.setInt(5, contract.getStartPrice());

                int effectiveRaws = preparedSt.executeUpdate();

                if (effectiveRaws == 1) {
                    ResultSet generatedKeys = preparedSt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        insertedContract = findContractById(conn, generatedKeys.getInt(ID_COLUMN));
                    }
                }
                conn.commit();
                return Optional.ofNullable(insertedContract);
            } catch (SQLException ex) {
                conn.rollback();
                throw new RepositoryException("Can't add Contract", ex);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Can't add Contract", ex);
        }
    }

    private Contract findContractById(Connection conn, int id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(FIND_CONTRACT_BY_ID_QUERY);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        Contract contract = null;
        if (resultSet.next()) {
            contract = createContract(resultSet);
        }
        return contract;
    }

    private Contract createContract(ResultSet resultSet) throws SQLException {
        Contract contract = new Contract();
        contract.setId(resultSet.getInt(ID_COLUMN));
        contract.setOwnerId(resultSet.getInt(OWNER_ID_COLUMN));
        contract.setDescription(resultSet.getString(DESCRIPTION_COLUMN));
        contract.setStartDate(LocalDate.parse(resultSet.getDate(START_DATE_COLUMN).toString()));
        contract.setEndDate(LocalDate.parse(resultSet.getDate(END_DATE_COLUMN).toString()));
        contract.setStartPrice(resultSet.getInt(START_PRICE_COLUMN));
        return contract;
    }

    private void removeAllOffersForContract(Connection conn, int contractId) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(REMOVE_ALL_OFFERS_FOR_CONTRACT_QUERY);
        preparedStatement.setInt(1, contractId);
        preparedStatement.execute();
    }

    private void validateContractData(Contract contract) throws RepositoryException {
        if (contract == null) {
            throw new RepositoryException("Contract can not be 'null'!");
        }
        if (contract.getDescription() == null || contract.getDescription().isEmpty()) {
            throw new RepositoryException("Contract field 'description' must not be null or empty!");
        }
        if (contract.getStartDate() == null || contract.getStartDate().isBefore(LocalDate.now())) {
            throw new RepositoryException("Contract field 'startDate' must not be null or before today " +
                    "(" + LocalDate.now() + ")!");
        }
        if (contract.getEndDate() == null || contract.getEndDate().isBefore(LocalDate.now())) {
            throw new RepositoryException("Contract field 'endDate' must not be null or before today " +
                    "(" + LocalDate.now() + ")!");
        }
        if (contract.getStartPrice() == null || contract.getStartPrice() < 0) {
            throw new RepositoryException("Contract field 'startPrice' must not be null or negative!");
        }
        if (contract.getEndDate().isBefore(contract.getStartDate())) {
            throw new RepositoryException("Contract start date should be before end date!");
        }
    }
}
