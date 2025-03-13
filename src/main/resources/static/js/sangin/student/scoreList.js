document.addEventListener("DOMContentLoaded", function () {
    loadScoreList();

    // ✅ 모달 닫기 버튼 이벤트 추가
    const closeModalBtn = document.getElementById("score_close_modal");
    if (closeModalBtn) {
        closeModalBtn.addEventListener("click", function () {
            document.getElementById("score_detail_modal").style.display = "none";
        });
    } else {
        console.error("🚨 오류: 'score_close_modal' 요소를 찾을 수 없습니다.");
    }
});

async function loadScoreList() {
    try {
        // ✅ 성적 리스트 + 상세 데이터 한 번에 가져오기
        const scoreResponse = await axios.get(`/api/student/scoreList`);
        const scoreList = scoreResponse.data;

        const tableBody = document.getElementById("score_table_body");
        if (!tableBody) {
            console.error("🚨 오류: 'score_table_body' 요소를 찾을 수 없습니다.");
            return;
        }

        tableBody.innerHTML = ""; // 기존 데이터 초기화

        scoreList.forEach((session) => {
            let difficultyText = '';
            switch (session.difficulty) {
                case 'EASY': difficultyText = '초급'; break;
                case 'MEDIUM': difficultyText = '중급'; break;
                case 'HARD': difficultyText = '고급'; break;
                case 'TEST': difficultyText = '시험'; break;
                default: difficultyText = 'ERROR';
            }

            // ✅ OX 결과 변환 (session.attemptDetails 활용)
            let oxResults = session.attemptDetails.map(attempt =>
                `<td class="ox_btn" data-problem-id="${attempt.problemId}">
                    ${attempt.isCorrect === 1 ? "⭕" : "❌"}
                </td>`).join("");

            // ✅ 테이블 행 추가
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${session.times} 회차</td>
                <td>${session.totalScore}점</td>
                <td>${difficultyText}</td>
                ${oxResults}
            `;
            tableBody.appendChild(row);

            // ✅ OX 버튼 클릭 시 문제 상세보기
            row.querySelectorAll(".ox_btn").forEach(btn => {
                btn.addEventListener("click", function () {
                    const problemId = this.dataset.problemId;
                    loadProblemDetail(problemId);
                });
            });
        });

    } catch (error) {
        console.error("🚨 성적 목록 불러오기 실패:", error);
    }
}

// ✅ 특정 문제 상세 조회 (OX 버튼 클릭 시)
async function loadProblemDetail(problemId) {
    try {
        const response = await axios.get(`/api/student/problemDetail?problemId=${problemId}`);
        const problemData = response.data;
        renderProblemDetail(problemData);
    } catch (error) {
        console.error("🚨 문제 상세 불러오기 실패:", error);
    }
}

// ✅ 상세보기 모달 데이터 렌더링
function renderProblemDetail(problemData) {
    const detailContainer = document.getElementById("score_detail_container");
    const link = document.getElementById("score_detail_link");
    if (!detailContainer) {
        console.error("🚨 오류: 'score_detail_container' 요소를 찾을 수 없습니다.");
        return;
    }

    detailContainer.innerHTML = `
        <div class="problem_title"><b>문제:</b> ${problemData.question}</div>
        <div class="problem_choice"><b>내가 선택한 답:</b> <span class="${problemData.isCorrect ? 'correct-answer' : 'wrong-answer'}">
            ${problemData.selectedChoiceText || "선택 없음"}
        </span></div>
        <div class="problem_correct"><b>정답:</b> <span class="correct-answer">${problemData.correctChoiceText}</span></div>
    `;
    link.addEventListener("click", function () {
        window.location.href=`/view/common/problemDetail/${problemData.problemId}`;
    })

    document.getElementById("score_detail_modal").style.display = "block"; // 모달 표시
}
