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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "service")
public class Service extends BaseEntity {

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "service_status")
    @Enumerated(EnumType.STRING)
    private ServiceStatus serviceStatus;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "created_by", nullable = false)
    @ManyToOne
    private User createdBy;

    @Column(name = "updated_by", nullable = false)
    @ManyToOne
    private User updatedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany
    private List<City> city;

    @ManyToOne
    private Category category;

}
