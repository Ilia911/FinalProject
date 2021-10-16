package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.repository.OfferRepository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OfferRepositoryImpl implements OfferRepository {

    private DataSource dataSource;

    private static final String ID_COLUMN = "id";
    private static final String OFFER_OWNER_ID_COLUMN = "offer_owner_id";
    private static final String CONTRACT_ID_COLUMN = "contract_id";
    private static final String PRICE_COLUMN = "price";
    private static final String FIND_OFFER_BY_ID_QUERY = "SELECT * FROM builder.offer where id = ?";
    private static final String FIND_ALL_OFFERS_BY_CONTRACT_ID_QUERY
            = "SELECT * FROM builder.offer where contract_id = ?";
    private static final String DELETE_OFFER_QUERY
            = "DELETE FROM builder.offer where id = ?;";
    private static final String UPDATE_OFFER_QUERY
            = "UPDATE builder.offer SET price = ? WHERE id = ?";
    private static final String ADD_OFFER_QUERY
            = "INSERT INTO builder.offer(offer_owner_id, contract_id, price) VALUES (?, ?, ?)";

    public OfferRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Offer> find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            final Offer offerById = findOfferById(conn, id);
            if (offerById != null) {
                return Optional.of(offerById);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Offer> findAll(int contractId) {
        List<Offer> resultList = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL_OFFERS_BY_CONTRACT_ID_QUERY);
            preparedStatement.setInt(1, contractId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Offer offer = new Offer(resultSet.getInt(ID_COLUMN), resultSet.getInt(OFFER_OWNER_ID_COLUMN),
                        resultSet.getInt(CONTRACT_ID_COLUMN), resultSet.getBigDecimal(PRICE_COLUMN));
                resultList.add(offer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return resultList;
    }

    @Override
    public boolean delete(int id) {
        try (final Connection conn = dataSource.getConnection()) {
            final PreparedStatement preparedStatement = conn.prepareStatement(DELETE_OFFER_QUERY);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Offer update(Offer offer) {
        Offer updatedOffer = null;
        try (final Connection conn = dataSource.getConnection()) {
            final PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_OFFER_QUERY);
            preparedStatement.setBigDecimal(1, offer.getPrice());
            preparedStatement.setInt(2, offer.getId());
            preparedStatement.execute();
            updatedOffer = findOfferById(conn, offer.getId());
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Optional<Offer> add(Offer offer) {
        try(final Connection conn = dataSource.getConnection()) {
            final PreparedStatement preparedStatement = conn.prepareStatement(ADD_OFFER_QUERY);
            preparedStatement.setInt(1, offer.getOfferOwnerId());
            preparedStatement.setInt(2, offer.getContractId());
            preparedStatement.setBigDecimal(3, offer.getPrice());
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final Offer newOffer = new Offer(resultSet.getInt(ID_COLUMN), resultSet.getInt(OFFER_OWNER_ID_COLUMN),
                        resultSet.getInt(CONTRACT_ID_COLUMN), resultSet.getBigDecimal(PRICE_COLUMN));
                return Optional.of(newOffer);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    private Offer findOfferById(Connection conn, int id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(FIND_OFFER_BY_ID_QUERY);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            Offer offer = new Offer(resultSet.getInt(ID_COLUMN), resultSet.getInt(OFFER_OWNER_ID_COLUMN),
                    resultSet.getInt(CONTRACT_ID_COLUMN), resultSet.getBigDecimal(PRICE_COLUMN));
            return offer;
        } else {
            return null;
        }
    }
}
