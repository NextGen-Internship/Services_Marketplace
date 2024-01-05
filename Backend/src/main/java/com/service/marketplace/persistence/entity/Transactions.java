package com.service.marketplace.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Transactions extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "offer_id")
    private int offerId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "transaction_status")
    private String transactionStatus;

    public Transactions(int id, int userId, int offerId, double amount, LocalDateTime timestamp, String transactionStatus) {
        this.id = id;
        this.userId = userId;
        this.offerId = offerId;
        this.amount = amount;
        this.timestamp = timestamp;
        this.transactionStatus = transactionStatus;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return userId;
    }

    public int getOffer_id() {
        return offerId;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int userId) {
        this.userId = userId;
    }

    public void setOffer_id(int offerId) {
        this.offerId = offerId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }
}
