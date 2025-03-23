package com.example.sangdaeng001sqlbank.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "tbl_problems")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer problemId;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 50)
    private String difficulty;

    @Column(name = "user_id")
    private Integer createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "permit_yn", columnDefinition = "CHAR(1) DEFAULT 'Y'")
    private Character permitYn;

    @OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<ProblemChoice> choices;
}
