document.addEventListener("DOMContentLoaded", function () {
    // 페이지 로드 시 기본값(EASY) 데이터 가져오기
    fetchWrongRateTop10("EASY");

    // 난이도별 문제 해결률 분석
    fetchCorrectRate();

    // 전체 학생 조회
    getStudentList();

    // 셀렉트 박스 변경 시 데이터 가져오기
    document.getElementById("difficultySelect").addEventListener("change", function () {
        const selectedDifficulty = this.value;
        fetchWrongRateTop10(selectedDifficulty);
    });

    //학생별 평균 점수 및 랭킹 분석
    fetchAverageScore();

    // 학생별 평균 점수 및 랭킹 분석 셀렉트 박스 선택시
    document.getElementById("difficultySelect2").addEventListener("change", function () {
        const selectedDifficulty = this.value;
        averageScoreUpdateChart(averageScoreData, selectedDifficulty);
    });

    // 초기 로딩 시 학생 목록 + 차트까지 호출됨
    fetchStudentList();

    document.getElementById("difficultySelect3").addEventListener("change", function () {
        selectedDifficulty = this.value;
        scoreChangeChart(scoreChangeData, selectedDifficulty, studentId);
    });

    document.getElementById("studentSelect").addEventListener("change", function () {
        studentId = parseInt(this.value);
        scoreChangeChart(scoreChangeData, selectedDifficulty, studentId);
    });


});

// 학생 목록 가져오기 + 셀렉트 박스 렌더링
function fetchStudentList() {
    const classNum = document.getElementById("current_classNum").textContent.trim();
    axios.get(`/api/admin/students/by-class?classNum=${classNum}`)
        .then(response => {
            studentList = response.data.sort((a, b) => a.userId - b.userId);

            const select = document.getElementById("studentSelect");
            select.innerHTML = "";

            studentList.forEach(student => {
                const option = document.createElement("option");
                option.value = student.userId;
                option.textContent = student.name;
                select.appendChild(option);
            });

            // 기본 선택값
            studentId = parseInt(studentList[0].userId);
            selectedDifficulty = "EASY";
            // 차트 호출
            fetchScoreChange();
        })
        .catch(error => {
            console.error("학생 리스트 가져오기 실패:", error);
        });
}
//기수 설정
document.getElementById("classConfirmBtn").addEventListener("click", function () {
    let classNum = document.getElementById("classNum").value;
    axios.put("/api/admin/class-num?classNum=" + classNum)
        .then(response => {
            alert(response.data.message);
            window.location.reload();
        })
        .catch(error => {
            console.error("오류 발생!", error);
        });
});

