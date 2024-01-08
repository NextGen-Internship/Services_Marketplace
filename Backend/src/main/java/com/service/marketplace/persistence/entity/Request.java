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

    @Column(name = "description")
    private String description;

    @Column(name = "created_by")
    private int createdBy;

    @Column(name = "updated_by")
    private int updatedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
