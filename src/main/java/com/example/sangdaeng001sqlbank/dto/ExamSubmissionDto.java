package com.example.sangdaeng001sqlbank.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExamSubmissionDto {
    private int userId;
    private List<ExamAnswerDto> answers;
    private String difficulty;
}
