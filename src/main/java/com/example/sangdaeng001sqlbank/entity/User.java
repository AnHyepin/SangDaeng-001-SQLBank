package com.example.sangdaeng001sqlbank.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // `AUTO_INCREMENT` 적용
    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false, unique = true, length = 50)
    private String username; // `VARCHAR(50)`, `NOT NULL`, `UNIQUE`

    @Column(nullable = false, length = 50)
    private String name; // `VARCHAR(50)`, `NOT NULL`

    @Column(nullable = false, length = 255)
    private String password; // `VARCHAR(255)`, `NOT NULL`

    @Column(nullable = false, unique = true, length = 100)
    private String email; // `VARCHAR(100)`, `NOT NULL`, `UNIQUE`

    @Column(nullable = false, length = 50)
    private String role = "ROLE_STUDENT"; // `VARCHAR(50)`, 기본값 `ROLE_STUDENT`

    @Column(name = "class_num")
    private Integer classNum;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // `TIMESTAMP`, 기본값 `NOW()`
}
