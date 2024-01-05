package com.service.marketplace.persistence.entity;

import jakarta.persistence.*;

@Entity
public class Notification extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "status")
    private String status;

    public Notification(int id, int userId, Type type, String status) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public Type getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
