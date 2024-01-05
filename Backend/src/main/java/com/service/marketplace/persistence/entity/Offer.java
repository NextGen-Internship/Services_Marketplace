package com.service.marketplace.persistence.entity;

import com.service.marketplace.persistence.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "offer")
public class Offer extends BaseEntity {

    @Column(name = "request_id", nullable = false)
    private int requestId;

    @Column(name = "description")
    private String description;

    @Column(name = "offer_price")
    private double offerPrice;

    @Column(name = "date")
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "offer_status")
    private OfferStatus offerStatus;

    @Column(name = "created_by")
    private int createdBy;

    @Column(name = "updated_by")
    private int updatedBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}


