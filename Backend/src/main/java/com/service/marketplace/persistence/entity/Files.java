package com.service.marketplace.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "files")
public class Files extends BaseEntity {

    @Column(name = "url")
    private String url;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;
}
