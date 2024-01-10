package com.service.marketplace.persistence.entity;

import com.service.marketplace.persistence.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@Table(name = "transactions")
public class Transactions extends BaseEntity {

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "transaction_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @OneToOne
    private Offer offer;

}
