package com.example.sangdaeng001sqlbank.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tbl_problem_choices")
@Getter
@Setter
@ToString(exclude = "problem")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_id")
    private Integer choiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problem_id")
    @JsonBackReference
    private Problem problem;

    @Column(name = "choice_text", nullable = false, columnDefinition = "TEXT")
    private String choiceText;

    @Column(name = "is_correct", nullable = false)
    private Integer isCorrect;
}
