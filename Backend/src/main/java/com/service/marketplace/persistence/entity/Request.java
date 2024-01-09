package com.service.marketplace.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "request")
public class Request extends BaseEntity {

    @Column(name = "description", nullable = false)
    private String description;

    @JoinColumn(name = "created_by", nullable = false)
    @ManyToOne
    private User createdBy;

    @JoinColumn(name = "updated_by", nullable = false)
    @ManyToOne
    private User updatedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "service_id", nullable = false)
    @ManyToOne
    private Service service;

}
