package com.service.marketplace.persistence.entity;

import com.service.marketplace.persistence.enums.NotificationStatus;
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

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    @Column(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

}
