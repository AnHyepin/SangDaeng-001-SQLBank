document.addEventListener("DOMContentLoaded", function () {
    loadProblemDetail();

    // ✅ 상태 변경 버튼 클릭 이벤트 추가
    document.getElementById("statusBtn").addEventListener("click", function () {
        toggleProblemStatus();
    });

    // ✅ 삭제 버튼 클릭 이벤트 추가
    document.getElementById("deleteBtn").addEventListener("click", function () {
        deleteProblem();
    });

    document.getElementById("updateBtn").addEventListener("click", function () {
        updateProblem();
    })
});

// ✅ 문제 상세 정보 불러오기
async function loadProblemDetail() {
    try {
        const problemId = document.getElementById("problem_id").value;

        if (!problemId) {
            console.error("🚨 오류: 문제 ID가 존재하지 않습니다.");
            return;
        }

        const response = await axios.get(`/api/common/problemDetail/${problemId}`);
        const problemData = response.data;

        // ✅ 난이도 변환 (EASY -> 초급, MEDIUM -> 중급, HARD -> 고급)
        const difficultyMap = {
            "EASY": "초급",
            "MEDIUM": "중급",
            "HARD": "고급"
        };
        const difficultyText = difficultyMap[problemData.difficulty] || "알 수 없음";

        // ✅ 상태 변환 (Y -> 활성화, N -> 비활성화)
        const permitYnMap = {
            "Y": "활성화",
            "N": "비활성화"
        };
        const permitYnText = permitYnMap[problemData.permitYn] || "알 수 없음";

        // ✅ ID로 HTML 요소 업데이트
        document.getElementById("problemId").textContent = problemData.problemId;
        document.getElementById("createdBy").textContent = problemData.createdBy;
        document.getElementById("difficulty").textContent = difficultyText; // 변환된 값 적용
        document.getElementById("createdAt").textContent = problemData.createdAt;
        document.getElementById("question").textContent = problemData.question;
        document.getElementById("description").textContent = problemData.description;
        document.getElementById("answer").textContent = problemData.answer;

        // ✅ 상태 버튼 업데이트
        const statusBtn = document.getElementById("statusBtn");
        statusBtn.textContent = permitYnText;
        statusBtn.dataset.status = problemData.permitYn;
        statusBtn.classList.toggle("active", problemData.permitYn === "Y");
        statusBtn.classList.toggle("inactive", problemData.permitYn === "N");

    } catch (error) {
        console.error("🚨 문제 상세 정보를 불러오는 데 실패했습니다:", error);
    }
}

// ✅ 문제 상태 변경 (활성화 ↔ 비활성화)
async function toggleProblemStatus() {
    try {
        const problemId = document.getElementById("problem_id").value;
        const statusBtn = document.getElementById("statusBtn");
        const currentStatus = statusBtn.dataset.status;
        const newStatus = currentStatus === "Y" ? "N" : "Y";

        await axios.put(`/api/teacher/updateStatus/${problemId}`, {permitYn: newStatus});

        // ✅ 상태 버튼 업데이트
        statusBtn.textContent = newStatus === "Y" ? "활성화" : "비활성화";
        statusBtn.dataset.status = newStatus;
        statusBtn.classList.toggle("active", newStatus === "Y");
        statusBtn.classList.toggle("inactive", newStatus === "N");

    } catch (error) {
        alert("🚨 상태 변경에 실패했습니다.");
        console.error(error);
    }
}

// ✅ 문제 삭제
async function deleteProblem() {
    try {
        const problemId = document.getElementById("problem_id").value;
        if (!confirm("정말로 이 문제를 삭제하시겠습니까?")) return;

        await axios.delete(`/api/teacher/delete/${problemId}`);
        alert("✅ 문제가 삭제되었습니다.");
        window.location.href = "/view/teacher/problemList"; // 문제 목록으로 이동

    } catch (error) {
        alert("🚨 문제 삭제에 실패했습니다.");
        console.error(error);
    }
}

async function updateProblem() {
    const problemId = document.getElementById("problem_id").value;
    if (!confirm("수정 페이지로 이동하시겠습니까?")) return;
    window.location.href = `/view/teacher/problemUpdate/${problemId}`;
}
