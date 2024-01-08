package com.service.marketplace.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review_media")
public class ReviewMedia extends BaseEntity {


    @Lob
    @Column(name = "photo", nullable = false)
    private byte[] photo;



}
