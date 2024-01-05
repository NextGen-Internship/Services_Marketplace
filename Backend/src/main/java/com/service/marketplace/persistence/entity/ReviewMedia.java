package com.service.marketplace.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review_media")
public class ReviewMedia extends BaseEntity {

    @Column(name = "review_id", nullable = false)
    private int reviewId;

    @Lob
    @Column(name = "photo", nullable = false)
    private byte[] photo;

}
