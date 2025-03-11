document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("submit_exam_btn").addEventListener("click", submitExam);
});

function submitExam() {
    const problems = document.querySelectorAll(".problem");
    const answers = [];
    const urlParams = new URLSearchParams(window.location.search);
    let difficulty = urlParams.get("difficulty");
    // 대문자로 변환하여 API 요청
    difficulty = difficulty.toUpperCase();

    problems.forEach(problem => {
        const problemId = problem.dataset.problemId; // ✅ 문제 ID 가져오기
        const selectedAnswer = problem.querySelector(`input[name="answer${problemId}"]:checked`);

        if (selectedAnswer) {
            answers.push({
                problemId: parseInt(problemId),
                selectedChoiceId: parseInt(selectedAnswer.value)
            });
        }
    });

    // ✅ 모든 문제에 대해 답변했는지 확인
    if (answers.length < problems.length) {
        alert("모든 문제를 풀어야 제출할 수 있습니다.");
        return;
    }

    axios.post("/api/student/submitExam", {answers: answers, difficulty: difficulty})
        .then(response => {
            alert(response.data);
            window.location.href = "/"; // 결과 페이지로 이동
        })
        .catch(error => {
            console.error("시험 제출 실패:", error);
            alert("시험 제출에 실패했습니다. 다시 시도해주세요.");
        });
}
