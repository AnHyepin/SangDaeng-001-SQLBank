document.addEventListener("DOMContentLoaded", function () {
    loadScoreList();
});

// ✅ 성적 리스트 불러오기 (비동기 병렬 처리)
async function loadScoreList() {
    try {
        const scoreResponse = await axios.get(`/api/student/scoreList`);
        const scoreList = scoreResponse.data;
        const tableBody = document.getElementById("score_table_body");
        tableBody.innerHTML = ""; // 기존 데이터 초기화

        // ✅ 모든 세션의 scoreDetail을 병렬 요청
        const detailRequests = scoreList.map(session => axios.get(`/api/student/scoreDetail?sessionId=${session.sessionId}`));
        const detailResponses = await Promise.all(detailRequests);

        scoreList.forEach((session, index) => {
            const attemptDetails = detailResponses[index].data;
            let difficultyText = '';

            switch (session.difficulty) {
                case 'EASY': difficultyText = '초급'; break;
                case 'MEDIUM': difficultyText = '중급'; break;
                case 'HARD': difficultyText = '고급'; break;
                case 'TEST': difficultyText = '시험'; break;
                default: difficultyText = 'ERROR';
            }

            // ✅ OX 결과 변환
            let oxResults = attemptDetails.map(attempt => attempt.isCorrect === 1 ? "⭕" : "❌").join('</td><td>');

            // ✅ 테이블 행 추가
            const row = document.createElement("tr");
            row.innerHTML = `
                <td class="detail_btn" data-session-id="${session.sessionId}">🔍</td>
                <td>${session.times} 회차</td>
                <td>${session.totalScore}점</td>
                <td>${difficultyText}</td>
                <td>${oxResults}</td>
            `;
            tableBody.appendChild(row);

            // ✅ 상세 보기 이벤트 추가
            row.querySelector(".detail_btn").addEventListener("click", function () {
                loadScoreDetail(session.sessionId);
            });
        });

    } catch (error) {
        console.error("성적 목록 불러오기 실패:", error);
    }
}

// ✅ 특정 회차의 문제별 정답 상세 조회
async function loadScoreDetail(sessionId) {
    try {
        const response = await axios.get(`/api/student/scoreDetail?sessionId=${sessionId}`);
        const sessionData = response.data;
        renderScoreDetail(sessionData);
    } catch (error) {
        console.error("회차 상세 정답 불러오기 실패:", error);
    }
}

// ✅ 상세보기 데이터 렌더링
function renderScoreDetail(sessionData) {
    const detailContainer = document.getElementById("score_detail_container");
    detailContainer.innerHTML = ""; // 기존 상세 초기화

    sessionData.forEach((attempt, index) => {
        const isCorrect = attempt.isCorrect === 1 ? "⭕" : "❌";
        const attemptDiv = document.createElement("div");
        attemptDiv.className = "attempt_row";
        attemptDiv.innerHTML = `
            <div class="attempt_text"><b>문제 ${index + 1}.</b></div>
            <div class="attempt_text">사용자 선택: <span>${attempt.selectedChoiceText || "선택 없음"}</span></div>
            <div class="attempt_text">정답: <span>${attempt.correctChoiceText}</span></div>
            <div class="attempt_text">결과: <span>${isCorrect}</span></div>
        `;
        detailContainer.appendChild(attemptDiv);
    });

    document.getElementById("score_detail_modal").style.display = "block"; // 모달 표시
}

// ✅ 모달 닫기 이벤트
document.getElementById("score_close_modal").addEventListener("click", function () {
    document.getElementById("score_detail_modal").style.display = "none";
});
