# SQL 문제은행 시스템

## 프로젝트 소개
SQL 문제은행 시스템은 교육자와 학습자를 위한 SQL 학습 플랫폼입니다. 실시간 랭킹 시스템, 자동 채점 기능, 난이도별 필터링 등 다양한 기능을 제공하여 효과적인 SQL 학습을 지원합니다.

## 주요 기능
- 실시간 랭킹 시스템
- 자동 채점 기능
- 정답률 기반 점수 계산
- 난이도별 필터링 (Beginner, Intermediate, Advanced)
- 반응형 시상대 UI
- 커뮤니티 기능 (게시글, 댓글)

## 기술 스택
- Backend: Spring Boot
- Frontend: Thymeleaf, JavaScript, CSS
- Database: MySQL
- Security: Spring Security, JWT
- Authentication: Cookie, Filter, Interceptor

## 설치 및 실행 방법

### 1. 데이터베이스 설정
```sql
# 1. 한번에 DB 생성 및 초기 데이터 입력
# 프로젝트 루트 디렉토리의 sql 파일 실행
```

### 2. 애플리케이션 설정
```properties
# application.properties 파일에서 데이터베이스 설정 수정
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## 사용자 역할 및 권한

### ROLE_STUDENT
- 문제 목록 조회
- 문제 상세 조회
- 문제 풀기 및 제출
- 성적 조회
- 게시글 CRUD
- 댓글 CRUD

### ROLE_TEACHER
- 문제 등록
- 문제 상태 변경 (비활성화 시 시험지에서 제외)
- (ADMIN이 권한 부여 시)

### ROLE_ADMIN
- 문제 수정/삭제
- 문제 상세 댓글 삭제
- 커뮤니티 게시글/댓글 삭제
- 관리자 페이지 접근
- TEACHER 권한 부여
- 통계 조회
- 기수 설정

## 관리자 계정
- ID: 1
- PW: 1234
- 관리자 페이지: 하단 footer의 '관리자' 링크 클릭

## 문제 해결
### 로그인 오류
- F12 -> Application -> Cookie 삭제 후 재시도

## 개발 예정 기능
- [ ] 타이머 기능
- [ ] 검색 기능
- [ ] 로그인 시간 타이머 & 연장 기능

## 문의 및 버그 리포트
- 연락처: 010-2552-9440, 010-5572-4186
- 이메일: gpqls9440@naver.com, hansam8456@naver.com
