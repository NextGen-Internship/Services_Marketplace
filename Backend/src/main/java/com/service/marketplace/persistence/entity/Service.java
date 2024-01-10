package com.service.marketplace.persistence.entity;

import com.service.marketplace.persistence.enums.ServiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Table(name = "service")
public class Service extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "service_status")
    @Enumerated(EnumType.STRING)
    private ServiceStatus serviceStatus;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    // TODO
    @JoinColumn(name = "provider_id", nullable = false)
    @ManyToOne
    private User provider;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_Active")
    private boolean isActive;

    @JoinColumn(name = "category_id", nullable = false)
    @ManyToOne
    private Category category;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "service_city",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "city_id")
    )
    private List<City> cities;


}
