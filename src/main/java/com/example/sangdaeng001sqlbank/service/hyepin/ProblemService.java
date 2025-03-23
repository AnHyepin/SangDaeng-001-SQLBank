package com.example.sangdaeng001sqlbank.service.hyepin;

import com.example.sangdaeng001sqlbank.entity.Problem;
import com.example.sangdaeng001sqlbank.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    public List<Problem> searchProblems(String type, String keyword) {
        switch (type) {
            case "id":
                return problemRepository.findByIdContaining(keyword);
            case "Q":
                return problemRepository.findByQuestionContaining(keyword);
            case "A":
                return problemRepository.findByCreatedByNameContaining(keyword);
            default:
                return new ArrayList<>();
        }
    }



}
