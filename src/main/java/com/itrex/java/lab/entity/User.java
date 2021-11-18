package com.itrex.java.lab.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "user", schema = "builder")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    private Role role;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_certificate", schema = "builder", joinColumns = {@JoinColumn(name = ("user_id"))},
            inverseJoinColumns = {@JoinColumn(name = ("certificate_id"))})
    private List<Certificate> certificates;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "offerOwner", cascade = CascadeType.REMOVE)
    private List<Offer> offers;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private List<Contract> contracts;
}
