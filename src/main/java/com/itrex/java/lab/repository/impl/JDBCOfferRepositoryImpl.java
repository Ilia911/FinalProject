package com.itrex.java.lab.repository.impl;

import com.itrex.java.lab.entity.Contract;
import com.itrex.java.lab.entity.Offer;
import com.itrex.java.lab.entity.User;
import com.itrex.java.lab.exeption.RepositoryException;
import com.itrex.java.lab.repository.OfferRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCOfferRepositoryImpl implements OfferRepository {

    @Autowired
    private final DataSource dataSource;

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

    public JDBCOfferRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Offer> find(int id) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            Offer offerById = findOfferById(conn, id);
            return Optional.ofNullable(offerById);
        } catch (SQLException ex) {
            throw new RepositoryException("Can't find Offer", ex);
        }
    }

    @Override
    public List<Offer> findAll(int contractId) throws RepositoryException {

        try (Connection conn = dataSource.getConnection()) {
            List<Offer> resultList = new ArrayList<>();
            PreparedStatement preparedStatement = conn.prepareStatement(FIND_ALL_OFFERS_BY_CONTRACT_ID_QUERY);
            preparedStatement.setInt(1, contractId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Offer offer = createOffer(resultSet);
                resultList.add(offer);
            }
            return resultList;
        } catch (SQLException ex) {
            throw new RepositoryException("Can't find Offers", ex);
        }
    }

    @Override
    public boolean delete(int id) throws RepositoryException {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(DELETE_OFFER_QUERY);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new RepositoryException("Can't delete Offers", ex);
        }
    }

    @Override
    public Offer update(Offer offer) throws RepositoryException {

        validateOfferData(offer);

        try (Connection conn = dataSource.getConnection()) {
            Offer updatedOffer;
            try {
                PreparedStatement preparedStatement = conn.prepareStatement(UPDATE_OFFER_QUERY);
                preparedStatement.setInt(1, offer.getPrice());
                preparedStatement.setInt(2, offer.getId());
                preparedStatement.execute();
                updatedOffer = findOfferById(conn, offer.getId());
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw new RepositoryException("Can't update Offer", ex);
            } finally {
                conn.setAutoCommit(true);
            }
            return updatedOffer;
        } catch (SQLException ex) {
            throw new RepositoryException("Can't update Offer", ex);
        }
    }

    @Override
    public Optional<Offer> add(Offer offer) throws RepositoryException {

        validateOfferData(offer);

        try (Connection conn = dataSource.getConnection()) {
            try {
                conn.setAutoCommit(false);
                PreparedStatement preparedStatement = conn.prepareStatement(ADD_OFFER_QUERY, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setInt(1, offer.getOfferOwner().getId());
                preparedStatement.setInt(2, offer.getContract().getId());
                preparedStatement.setInt(3, offer.getPrice());
                int effectiveRaws = preparedStatement.executeUpdate();

                Offer newOffer = null;
                if (effectiveRaws == 1) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        newOffer = findOfferById(conn, generatedKeys.getInt(ID_COLUMN));
                    }
                }
                return Optional.ofNullable(newOffer);
            } catch (SQLException ex) {
                conn.rollback();
                throw new RepositoryException("Can't update Offer", ex);
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            throw new RepositoryException("Can't update Offer", ex);
        }
    }

    private Offer findOfferById(Connection conn, int id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(FIND_OFFER_BY_ID_QUERY);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();

        Offer offer = null;
        if (resultSet.next()) {
            offer = createOffer(resultSet);
        }
        return offer;
    }

    private Offer createOffer(ResultSet resultSet) throws SQLException {
        Offer offer = new Offer();
        offer.setId(resultSet.getInt(ID_COLUMN));
        offer.setOfferOwner(createEmptyUserWithId(resultSet.getInt(OFFER_OWNER_ID_COLUMN)));
        offer.setContract(createEmptyContractWithId(resultSet.getInt(CONTRACT_ID_COLUMN)));
        offer.setPrice(resultSet.getInt(PRICE_COLUMN));
        return offer;
    }

    private User createEmptyUserWithId(int id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    private Contract createEmptyContractWithId(int id) {
        Contract contract = new Contract();
        contract.setId(id);
        return contract;
    }

    private void validateOfferData(Offer offer) throws RepositoryException {
        if (offer == null) {
            throw new RepositoryException("Offer can not be null!");
        }
        if (offer.getPrice() == null || offer.getPrice() <= 0) {
            throw new RepositoryException("Offer field 'price' must not be null or empty!");
        }
    }
}
