package com.service.marketplace.persistence.entity;

import com.service.marketplace.persistence.enums.SourceType;
import jakarta.persistence.*;
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

    @Column(name = "source_id")
    private Integer sourceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "source_type")
    private SourceType sourceType;
}
