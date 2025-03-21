document.addEventListener("DOMContentLoaded", function () {
    loadProblems();
});

async function loadProblems() {
    // URL에서 난이도 가져오기
    const urlParams = new URLSearchParams(window.location.search);
    let difficulty = urlParams.get("difficulty");

    if (!difficulty) {
        alert("잘못된 접근입니다.");
        return;
    }

    const mainHeaderDifficulty = document.getElementById("main_header_box3");
    const box1Left = document.getElementById("box1_left");
    let difficultyText = '';

    switch (difficulty) {
        case 'easy':
            difficultyText = '초급 영역';
            break;
        case 'medium':
            difficultyText = '중급 영역';
            break;
        case 'hard':
            difficultyText = '고급 영역';
            break;
        case 'test':
            difficultyText = '모의고사 영역';
            box1Left.innerHTML = '시험용';
            break;
        default:
            difficultyText = '잘못된 접근 영역';
    }

    mainHeaderDifficulty.textContent = difficultyText;

    // 대문자로 변환하여 API 요청
    difficulty = difficulty.toUpperCase();

    try {
        const response = await axios.get(`/api/student/loadExam?difficulty=${difficulty}`);
        const problems = response.data;

        if (problems.length === 0) {
            alert("해당 난이도의 문제가 없습니다.");
            return;
        }

        await renderProblemsSequentially(problems); // 문제를 하나씩 로드
    } catch (error) {
        console.error("문제 불러오기 실패:", error);
    }
}

async function renderProblemsSequentially(problems) {
    const leftSection = document.getElementById("main_left_section");
    const rightSection = document.getElementById("main_right_section");

    leftSection.innerHTML = "";
    rightSection.innerHTML = "";

    const numberToKorean = ["①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧"];

    for (let index = 0; index < problems.length; index++) {
        const problem = problems[index];

        const problemDiv = document.createElement("div");
        problemDiv.className = "problem";
        problemDiv.dataset.problemId = problem.problemId;
        // <span className="description">${problem.description}</span>

        problemDiv.innerHTML = `
            <div class="problem_metadata">
                <span class="problem_id">문제 고유 ID : ${problem.problemId}</span>
                <span class="created_by">출제자 : ${problem.createdByName}</span>
                <input type="hidden" class="hidden_problem_id" value="${problem.problemId}">
            </div>
            <div class="problem_question_box">
                <span class="problem_no">${index + 1}.</span>
                <span class="question">${problem.question}</span>
            </div>
            <div class="choice_box">
                ${problem.choices ? problem.choices.map((choice, i) => `
                    <label class="choice">
                        <input type="radio" name="answer${problem.problemId}" value="${choice.choiceId}">
                        <span class="radio_custom">${numberToKorean[i]}</span>
                        <span class="choice_context">${choice.choiceText}</span>
                    </label>
                `).join("") : "<p class='no-choice'>선택지가 없습니다.</p>"}
            </div>
        `;

        if (index < 5) {
            leftSection.appendChild(problemDiv);
        } else {
            rightSection.appendChild(problemDiv);
        }

        // **비동기 로딩을 위해 약간의 지연을 줘서 순차적 렌더링을 구현**
        await new Promise(resolve => setTimeout(resolve, 1)); // 100ms 대기 후 다음 문제 렌더링
    }
}
