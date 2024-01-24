package com.service.marketplace.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@RequiredArgsConstructor
@Table(name = "subscription")
public class Subscription extends BaseEntity {
    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne
    private User user;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "isActive", nullable = false)
    private boolean isActive = true;
}
