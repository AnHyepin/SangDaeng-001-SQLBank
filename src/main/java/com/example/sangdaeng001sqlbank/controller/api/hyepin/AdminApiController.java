package com.example.sangdaeng001sqlbank.controller.api.hyepin;

import com.example.sangdaeng001sqlbank.dto.CorrectDto;
import com.example.sangdaeng001sqlbank.dto.UserDto;
import com.example.sangdaeng001sqlbank.dto.WrongRateDto;
import com.example.sangdaeng001sqlbank.entity.ClassSetting;
import com.example.sangdaeng001sqlbank.entity.User;
import com.example.sangdaeng001sqlbank.repository.ClassSettingRepository;
import com.example.sangdaeng001sqlbank.repository.UserRepository;
import com.example.sangdaeng001sqlbank.service.hyepin.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Slf4j
public class AdminApiController {

    private final ClassSettingRepository classSettingRepository;
    private final UserRepository userRepository;
    private final AdminService adminService;

    //현재 기수 조회
    @GetMapping("/class-num")
    public ResponseEntity<Integer> getClassNum() {
        return new ResponseEntity<>(classSettingRepository.findClassNum(), HttpStatus.OK);
    }

    //기수 업데이트
    @PutMapping("/class-num")
    public ResponseEntity<Map<String, String>> updateClassNum(@RequestParam Integer classNum) {
        ClassSetting classSetting = new ClassSetting();
        classSetting.setId(1);
        classSetting.setClassNum(classNum);
        ClassSetting updatedSetting = classSettingRepository.save(classSetting); // 업데이트된 값 반환

        if (updatedSetting.getClassNum() == classNum) {
            return ResponseEntity.ok(Map.of("message", "기수 업데이트 성공! 새로운 기수: " + classNum));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "업데이트 실패: 값이 반영되지 않았습니다."));
        }
    }

    //문제별 오답률 TOP 10
    @GetMapping("/wrong-rate")
    public  ResponseEntity<List<WrongRateDto>> getWrongRateList(@RequestParam String difficulty) {
        List<WrongRateDto> wrongRateList = adminService.getWrongRateList(difficulty);
        if (wrongRateList.isEmpty()) {
            return ResponseEntity.noContent().build(); // 데이터가 없으면 204 응답 반환
        }
        return ResponseEntity.ok(wrongRateList); // 정상 응답 시 200 OK와 함께 데이터 반환
    }

    //난이도별 문제 해결률 분석
    @GetMapping("/correct-rate")
    public ResponseEntity<List<CorrectDto>> getCorrectRate() {
        List<CorrectDto> wrongRateList = adminService.getCorrectRate();
        if (wrongRateList.isEmpty()) {
            return ResponseEntity.noContent().build(); // 데이터가 없으면 204 응답 반환
        }
        return ResponseEntity.ok(wrongRateList); // 정상 응답 시 200 OK와 함께 데이터 반환
    }

    //전체 학생 조회
    @GetMapping("students")
    public ResponseEntity<Page<User>> getPagedUserList(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("userId").descending());
        Page<User> userPage = userRepository.findByRoleNot("ROLE_ADMIN", pageable);
        if (userPage.isEmpty()) {
            return ResponseEntity.noContent().build(); // 데이터가 없으면 204 응답 반환
        }
        return ResponseEntity.ok(userPage); // 정상 응답 시 200 OK와 함께 데이터 반환
    }

    //기수별 학생 조회
    @GetMapping("students/by-class")
    public ResponseEntity<List<User>> getUserListByClassNum(@RequestParam Integer classNum) {
        List<User> userList = userRepository.findByClassNumAndRoleNotOrderByUserIdDesc(classNum, "ROLE_ADMIN");
        if (userList.isEmpty()) {
            return ResponseEntity.noContent().build(); // 데이터가 없으면 204 응답 반환
        }
        return ResponseEntity.ok(userList); // 정상 응답 시 200 OK와 함께 데이터 반환
    }

    //권한 수정
    @PostMapping("authority")
    public ResponseEntity<?> updateAuthority(@RequestBody User user) {
        log.info("user {}", user.toString());
        if(adminService.updateAuthority(user) != null) {
            return ResponseEntity.ok().body(Map.of("message", "권한 변경이 완료되었습니다."));
        }else{
           return ResponseEntity.ok().body(Map.of("message", "권한 변경 실패!"));
        }
    }

    //학생별 평균 점수 및 랭킹 분석
    @GetMapping("average-score")
    public ResponseEntity<?> getAverageScore() {
        if (adminService.getAverageScore() == null) {
            return ResponseEntity.noContent().build(); // 데이터가 없으면 204 응답 반환
        }
        return ResponseEntity.ok(adminService.getAverageScore()); // 정상 응답 시 200 OK와 함께 데이터 반환
    }

    //학생별 점수 변화
    @GetMapping("score-change")
    public ResponseEntity<?> getScoreChange() {
        if (adminService.getScoreChange() == null) {
            return ResponseEntity.noContent().build(); // 데이터가 없으면 204 응답 반환
        }
        return ResponseEntity.ok(adminService.getScoreChange()); // 정상 응답 시 200 OK와 함께 데이터 반환
    }


}

