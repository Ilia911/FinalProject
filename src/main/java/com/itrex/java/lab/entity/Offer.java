package com.itrex.java.lab.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "offer", schema = "builder")
public class Offer {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private int id;
    @Column (name = "offer_owner_id")
    private int offerOwnerId;
    @Column (name = "contract_id")
    private int contractId;
    @Column (name = "price")
    private Integer price;

    public Offer() {
    }

    public Offer(int id, int offerOwnerId, int contractId, Integer price) {
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
