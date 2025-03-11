document.addEventListener("DOMContentLoaded", function () {
    loadProblemData();
});

function loadProblemData() {
    // hidden input에서 problemId 가져오기
    const problemId = document.getElementById('problem_id').value;

    axios.get(`/api/teacher/problemDetail/${problemId}`)
        .then(response => {
            const problem = response.data;

            // 문제 질문, 설명, 난이도 설정
            document.querySelector('[name="question"]').value = problem.question;
            document.querySelector('[name="description"]').value = problem.description;
            document.getElementById('main_info_difficulty').value = problem.difficulty;

            // 보기 데이터 렌더링
            const choiceSection = document.getElementById('main_choice_section');
            choiceSection.innerHTML = ''; // 기존 보기 초기화

            problem.choices.forEach((choice, index) => {
                addChoiceItem(choice.choiceText, index + 1, choice.isCorrect);
            });
        })
        .catch(error => {
            console.error("문제 데이터를 불러오는 데 실패했습니다.", error);
        });
}

// ✅ 보기 추가 버튼 클릭 이벤트
document.getElementById('add_choice_btn').addEventListener('click', function () {
    const choiceCount = document.querySelectorAll('.choice_item').length + 1;
    addChoiceItem('', choiceCount, false);
});

// ✅ 보기 추가 함수
function addChoiceItem(choiceText, choiceNumber, isCorrect) {
    const choiceSection = document.getElementById('main_choice_section');

    const choiceDiv = document.createElement('div');
    choiceDiv.className = 'choice_item';

    const isChecked = isCorrect ? 'checked' : '';

    choiceDiv.innerHTML = `
        <label>
            <input type="radio" name="correctChoice" value="${choiceNumber}" ${isChecked}>
            <span>보기 ${choiceNumber}${isChecked ? ' - 정답' : ''}</span>
        </label>
        <div class="choice_box1">
            <textarea class="main_choice_input">${choiceText}</textarea>
            <button type="button" class="delete_choice_btn">삭제</button>
        </div>
    `;

    choiceSection.appendChild(choiceDiv);

    // 삭제 버튼 클릭 이벤트 추가
    choiceDiv.querySelector('.delete_choice_btn').addEventListener('click', function () {
        choiceDiv.remove();
        updateChoiceNumbers();
    });
}

// ✅ 정답 선택 시 '- 정답' 표시 추가
document.getElementById('main_choice_section').addEventListener('change', function (e) {
    if (e.target.name === 'correctChoice') {
        document.querySelectorAll('.choice_item label span').forEach(span => {
            span.textContent = span.textContent.replace(' - 정답', '');
        });

        const selectedSpan = e.target.nextElementSibling;
        selectedSpan.textContent += ' - 정답';
    }
});

// ✅ 보기 삭제 후 번호 업데이트
function updateChoiceNumbers() {
    const choiceItems = document.querySelectorAll('.choice_item');
    choiceItems.forEach((item, index) => {
        const label = item.querySelector('label span');
        const radio = item.querySelector('input[type="radio"]');
        radio.value = index + 1;
        label.textContent = `보기 ${index + 1}`;
    });
}
