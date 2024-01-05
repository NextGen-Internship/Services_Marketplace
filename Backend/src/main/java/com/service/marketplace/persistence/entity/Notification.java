package com.service.marketplace.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity {

    @Column(name = "user_id")
    private int userId;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "content")
    private String content;


    @Column(name = "status")
    private String status;


    public enum Type {

    }
}
