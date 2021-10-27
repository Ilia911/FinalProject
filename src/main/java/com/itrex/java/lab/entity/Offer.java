package com.itrex.java.lab.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "offer", schema = "builder")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "offer_owner_id")
    private User offerOwner;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @Column(name = "price")
    private Integer price;

    public Offer() {
    }

    public Offer(int id, User offerOwner, Contract contract, Integer price) {
        this.id = id;
        this.offerOwner = offerOwner;
        this.contract = contract;
        this.price = price;
    }

    public void removeContract() {
        this.contract = null;
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", offerOwner=" + offerOwner +
                ", contract=" + contract +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer offer = (Offer) o;
        return id == offer.id && Objects.equals(offerOwner, offer.offerOwner) && Objects.equals(contract, offer.contract) && Objects.equals(price, offer.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, offerOwner, contract, price);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getOfferOwner() {
        return offerOwner;
    }

    public void setOfferOwner(User offerOwner) {
        this.offerOwner = offerOwner;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
