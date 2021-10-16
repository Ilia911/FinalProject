package com.itrex.java.lab.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Contract {

    private int id;
    private int ownerId;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal startPrice;

    public Contract() {
    }

    public Contract(int id, int ownerId, String description, LocalDate startDate, LocalDate endDate, BigDecimal startPrice) {
        this.id = id;
        this.ownerId = ownerId;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startPrice = startPrice;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", startPrice=" + startPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contract contract = (Contract) o;
        return id == contract.id && ownerId == contract.ownerId && Objects.equals(description, contract.description) && Objects.equals(startDate, contract.startDate) && Objects.equals(endDate, contract.endDate) && Objects.equals(startPrice, contract.startPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, description, startDate, endDate, startPrice);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
    }
}
