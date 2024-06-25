package com.lfhardware.logging.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_audit")
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    private Object before;

    @JdbcTypeCode(SqlTypes.JSON)
    private Object after;

    @JdbcTypeCode(SqlTypes.JSON)
    private Object difference;

    @Column(length =  100)
    private String type;

    @Column(length = 20)
    private String action;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
