package com.itrex.java.lab.entity;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contract", schema = "builder")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "start_price", nullable = false)
    private Integer startPrice;

    @OneToMany(mappedBy = "contract")
    private List<Offer> offers;

    @PreRemove
    public void preRemoveOffers() {
        for (Offer offer : offers) {
            offer.removeContract();
        }
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + id +
                ", ownerId=" + owner.getId() +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", startPrice=" + startPrice +
                '}';
    }
}
