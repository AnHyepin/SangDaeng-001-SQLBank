package com.example.sangdaeng001sqlbank.service.sangin;

import com.example.sangdaeng001sqlbank.dao.sangin.StudentDao_sangin;
import com.example.sangdaeng001sqlbank.dto.ProblemChoiceDto;
import com.example.sangdaeng001sqlbank.dto.ProblemDto;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class StudentService_sangin {
    private final StudentDao_sangin studentDao;
    private final Random random = new Random(); // 랜덤 객체

    public StudentService_sangin(StudentDao_sangin studentDao) {
        this.studentDao = studentDao;
    }

    public List<ProblemDto> getProblemsByDifficulty(String difficulty) {
        List<ProblemDto> problemDtos;

        if ("TEST".equalsIgnoreCase(difficulty)) {
            // ✅ 시험 모드: 초급 3개, 중급 4개, 고급 3개 랜덤으로 가져오기
            List<ProblemDto> easyProblems = getRandomProblems("EASY", 3);
            List<ProblemDto> mediumProblems = getRandomProblems("MEDIUM", 4);
            List<ProblemDto> hardProblems = getRandomProblems("HARD", 3);

            problemDtos = easyProblems;
            problemDtos.addAll(mediumProblems);
            problemDtos.addAll(hardProblems);
        } else {
            // ✅ 일반 난이도 선택 시 기존 로직 유지
            problemDtos = getRandomProblems(difficulty, 10);
        }

        // ✅ 문제의 선택지를 세팅하는 로직 실행
        processChoicesForProblems(problemDtos);
        return problemDtos;
    }

    // ✅ 특정 난이도의 문제를 랜덤으로 가져오기 (최대 count개)
    private List<ProblemDto> getRandomProblems(String difficulty, int count) {
        List<ProblemDto> problems = studentDao.getProblemsByDifficulty(difficulty);
        if (problems.size() > count) {
            // 랜덤으로 count개 선택
            return random.ints(0, problems.size())
                    .distinct()
                    .limit(count)
                    .mapToObj(problems::get)
                    .collect(Collectors.toList());
        }
        return problems; // 문제가 적으면 있는 것만 반환
    }

    // ✅ 선택지를 처리하여 정답 1개 + 오답 3개만 포함하도록 보장
    private void processChoicesForProblems(List<ProblemDto> problemDtos) {
        for (ProblemDto problemDto : problemDtos) {
            List<ProblemChoiceDto> choiceDtos = studentDao.getProblemChoicesByDifficulty(problemDto.getProblemId());

            // ✅ 정답 선택
            List<ProblemChoiceDto> correctChoices = choiceDtos.stream()
                    .filter(choice -> choice.getIsCorrect() == 1)
                    .collect(Collectors.toList());

            // ✅ 오답 선택
            List<ProblemChoiceDto> incorrectChoices = choiceDtos.stream()
                    .filter(choice -> choice.getIsCorrect() == 0)
                    .collect(Collectors.toList());

            if (correctChoices.isEmpty()) continue; // 정답이 없으면 스킵

            // ✅ 정답 1개 랜덤 선택
            ProblemChoiceDto correctChoice = correctChoices.get(random.nextInt(correctChoices.size()));

            // ✅ 오답 3개 랜덤 선택 (오답 개수가 3개 미만이면 가능한 만큼 선택)
            List<ProblemChoiceDto> selectedIncorrectChoices;
            if (incorrectChoices.size() > 3) {
                selectedIncorrectChoices = random.ints(0, incorrectChoices.size())
                        .distinct()
                        .limit(3)
                        .mapToObj(incorrectChoices::get)
                        .collect(Collectors.toList());
            } else {
                selectedIncorrectChoices = incorrectChoices;
            }

            // ✅ 최종 보기를 리스트에 추가 후 섞기
            List<ProblemChoiceDto> finalChoices = selectedIncorrectChoices;
            finalChoices.add(correctChoice);
            java.util.Collections.shuffle(finalChoices);

            problemDto.setChoices(finalChoices); // 최종 보기를 문제에 추가
        }
    }
}
