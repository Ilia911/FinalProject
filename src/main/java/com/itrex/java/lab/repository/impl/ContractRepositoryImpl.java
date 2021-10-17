package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.repository.ContractRepository;

import javax.sql.DataSource;
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

public class ContractRepositoryImpl implements ContractRepository {

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

    public ContractRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Contract> find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            final Contract contract = findContractById(conn, id);

            if (contract != null) {
                return Optional.of(contract);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Contract> findAll() {
        List<Contract> resultList = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(FIND_ALL_CONTRACTS_QUERY);

            while (resultSet.next()) {
                Contract contract = new Contract(resultSet.getInt(ID_COLUMN),
                        resultSet.getInt(OWNER_ID_COLUMN),
                        resultSet.getString(DESCRIPTION_COLUMN),
                        LocalDate.parse(resultSet.getDate(START_DATE_COLUMN).toString()),
                        LocalDate.parse(resultSet.getDate(END_DATE_COLUMN).toString()),
                        resultSet.getInt(START_PRICE_COLUMN));
                resultList.add(contract);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultList;
    }

    @Override
    public boolean delete(int id) {
        try (final Connection conn = dataSource.getConnection()) {
            final PreparedStatement preparedStatement = conn.prepareStatement(DELETE_CONTRACT_QUERY);
            preparedStatement.setInt(1, id);
            return 1 == preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Contract update(Contract contract) {
        try (final Connection conn = dataSource.getConnection()) {
            final PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_CONTRACT_QUERY);
            preparedStatement.setString(1, contract.getDescription());
            preparedStatement.setDate(2, Date.valueOf(contract.getStartDate().toString()));
            preparedStatement.setDate(3, Date.valueOf(contract.getEndDate().toString()));
            preparedStatement.setInt(4, contract.getStartPrice());
            preparedStatement.setInt(5, contract.getId());
            preparedStatement.execute();

            return findContractById(conn, contract.getId());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Contract> add(Contract contract) {
        try (Connection conn = dataSource.getConnection()) {
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
                    return Optional.of(findContractById(conn, generatedKeys.getInt(ID_COLUMN)));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    private Contract findContractById(Connection conn, int id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(FIND_CONTRACT_BY_ID_QUERY);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return new Contract(resultSet.getInt(ID_COLUMN),
                    resultSet.getInt(OWNER_ID_COLUMN),
                    resultSet.getString(DESCRIPTION_COLUMN),
                    LocalDate.parse(resultSet.getDate(START_DATE_COLUMN).toString()),
                    LocalDate.parse(resultSet.getDate(END_DATE_COLUMN).toString()),
                    resultSet.getInt(START_PRICE_COLUMN));
        } else {
            return null;
        }
    }
}
