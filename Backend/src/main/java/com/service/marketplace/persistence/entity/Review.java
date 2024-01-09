package com.service.marketplace.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review extends BaseEntity {

    @Column(name = "description")
    private String description;

    @Column(name = "rating", nullable = false)
    private double rating;

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

    @Column(name = "files")
    @ManyToMany
    private List<Files> filesList;

}
