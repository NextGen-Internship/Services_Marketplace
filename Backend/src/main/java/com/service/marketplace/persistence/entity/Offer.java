package com.service.marketplace.persistence.entity;

import com.service.marketplace.persistence.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Table(name = "offer")
public class Offer extends BaseEntity {

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "offer_price", nullable = false)
    private BigDecimal offerPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "offer_status", nullable = false)
    private OfferStatus offerStatus;

    @JoinColumn(name = "provider_id", nullable = false)
    @ManyToOne
    private User provider;


    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrimaryKeyJoinColumn(name = "request_id")
    @OneToOne
    private Request request;

}


