package com.itrex.java.lab.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Offer {

    private int id;
    private int offerOwnerId;
    private int contractId;
    private BigDecimal price;

    public Offer() {
    }

    public Offer(int id, int offerOwnerId, int contractId, BigDecimal price) {
        this.id = id;
        this.offerOwnerId = offerOwnerId;
        this.contractId = contractId;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", offerOwnerId=" + offerOwnerId +
                ", contractId=" + contractId +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return id == offer.id && offerOwnerId == offer.offerOwnerId && contractId == offer.contractId && Objects.equals(price, offer.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, offerOwnerId, contractId, price);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOfferOwnerId() {
        return offerOwnerId;
    }

    public void setOfferOwnerId(int offerOwnerId) {
        this.offerOwnerId = offerOwnerId;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
