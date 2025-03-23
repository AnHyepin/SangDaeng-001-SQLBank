package com.example.sangdaeng001sqlbank.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_problem_choices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer choiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id", nullable = false)
    @JsonBackReference
    private Problem problem;

    @Column(name = "choice_text", nullable = false, columnDefinition = "TEXT")
    private String choiceText;

    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;
}