package com.service.marketplace.persistence.entity;

import com.service.marketplace.persistence.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "content")
    private String content;


    @Column(name = "status")
    private String status;



}
