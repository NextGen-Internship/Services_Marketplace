package com.service.marketplace.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@Table(name = "request")
public class Request extends BaseEntity {

    @Column(name = "description", nullable = false)
    private String description;

    @JoinColumn(name = "customer_id", nullable = true)
    @ManyToOne
    private User customer;


    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_Active")
    private boolean isActive = true;

    @JoinColumn(name = "service_id", nullable = false)
    @ManyToOne
    private Service service;

}
