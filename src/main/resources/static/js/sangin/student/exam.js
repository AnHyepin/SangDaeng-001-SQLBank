document.addEventListener("DOMContentLoaded", function () {
    loadProblems();
});

function loadProblems() {
    // URL에서 난이도 가져오기 (difficulty=easy 형태)
    const urlParams = new URLSearchParams(window.location.search);
    let difficulty = urlParams.get("difficulty");

    if (!difficulty) {
        alert("잘못된 접근입니다.");
        return;
    }
    const mainHeaderDifficulty = document.getElementById("main_header_box3");
    const box1Left = document.getElementById("box1_left");
    let difficultyText = '';
    if(difficulty == 'easy') {
        difficultyText = '초급 영역';
    }else if(difficulty == 'medium') {
        difficultyText = '중급 영역';
    }else if(difficulty == 'hard') {
        difficultyText = '고급 영역';
    }else if(difficulty == 'test') {
        difficultyText = '모의고사 영역';
        box1Left.innerHTML = '시험용';
    }else{
        difficultyText = '잘못된 접근 영역';
    }
    mainHeaderDifficulty.textContent = difficultyText;

    // URL에서 가져온 difficulty 값을 대문자로 변환하여 처리
    difficulty = difficulty.toUpperCase();

    // 난이도에 따라 문제 요청
    axios.get(`/api/student/exam?difficulty=${difficulty}`)
        .then(response => {
            const problems = response.data;
            if (problems.length === 0) {
                alert("해당 난이도의 문제가 없습니다.");
                return;
            }

            // 문제 개수 제한 (최대 10개)
            const selectedProblems = problems.slice(0, 10);
            renderProblems(selectedProblems);
        })
        .catch(error => {
            console.error("문제 불러오기 실패:", error);
        });
}

function renderProblems(problems) {
    const leftSection = document.getElementById("main_left_section");
    const rightSection = document.getElementById("main_right_section");

    leftSection.innerHTML = "";
    rightSection.innerHTML = "";

    const numberToKorean = ["①", "②", "③", "④"]; // 보기에 사용할 한글 숫자 배열

    problems.forEach((problem, index) => {
        const problemDiv = document.createElement("div");
        problemDiv.className = "problem";
        problemDiv.innerHTML = `
            <div class="problem_metadata">
                <span class="problem_id">문제 고유 ID : ${problem.problemId}</span>
                <span class="created_by">출제자 : ${problem.createdBy}</span>
<!--                <span class="difficulty">난이도 : ${problem.difficulty}</span>-->
            </div>
            <div class="problem_question_box">
                <span class="problem_no">${index + 1}.</span>
                <span class="question">${problem.question}</span>
            </div>
            <span class="description">${problem.description}</span>
            <div class="choice_box">
                ${problem.choices.map((choice, i) => `
                    <label class="choice">
                        <input type="radio" name="answer${problem.problemId}" value="${i + 1}">
                        <span class="radio_custom">${numberToKorean[i]}</span> ${choice.choiceText}
                    </label>
                `).join("")}
            </div>
        `;

        // 왼쪽 5문제, 오른쪽 5문제
        if (index < 5) {
            leftSection.appendChild(problemDiv);
        } else {
            rightSection.appendChild(problemDiv);
        }
    });
}
