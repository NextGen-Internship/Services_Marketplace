package com.service.marketplace.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "city")
public class City extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "zip_code", nullable = false)
    private int zipCode;

    @Column(name = "address", nullable = false)
    private String address;

    @ManyToMany(mappedBy = "cities")
    private List<Service> services;

}
