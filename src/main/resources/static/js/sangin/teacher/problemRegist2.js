// 보기 추가 버튼 클릭 이벤트
document.getElementById('add_choice_btn').addEventListener('click', function () {
    const choiceSection = document.getElementById('main_choice_section');
    const choiceCount = choiceSection.querySelectorAll('.choice_item').length + 1;

    const choiceDiv = document.createElement('div');
    choiceDiv.className = 'choice_item';
    choiceDiv.innerHTML = `
        <label>
            <input type="radio" name="correctChoice" value="${choiceCount}">
            <span>보기 ${choiceCount}</span>
        </label>
        <textarea class="main_choice_input"></textarea>
    `;
    choiceSection.appendChild(choiceDiv);
});

// 정답 설정 시 '- 정답' 표시 추가
document.getElementById('main_choice_section').addEventListener('change', function(e) {
    if (e.target.name === 'correctChoice') {
        document.querySelectorAll('.choice_item label span').forEach(span => {
            span.textContent = span.textContent.replace(' - 정답', '');
        });

        const selectedSpan = e.target.nextElementSibling;
        selectedSpan.textContent += ' - 정답';
    }
});

// 문제 등록 버튼 클릭 이벤트
document.getElementById('regist_btn').addEventListener('click', function () {
    const question = document.querySelector('[name="question"]').value;
    const description = document.querySelector('[name="description"]').value;
    const difficulty = document.getElementById('main_info_difficulty').value;

    const choiceItems = document.querySelectorAll('.choice_item');
    const choices = [];

    const correctChoiceRadio = document.querySelector('input[name="correctChoice"]:checked');
    const correctChoiceIndex = correctChoiceRadio ? parseInt(correctChoiceRadio.value) : -1;

    choiceItems.forEach((item, index) => {
        const choiceText = item.querySelector('.main_choice_input').value;
        choices.push({
            choiceText: choiceText,
            isCorrect: (index + 1 === correctChoiceIndex) ? 1 : 0
        });
    });

    // 유효성 검사
    if (!question.trim() || !description || choices.length < 5 || correctChoiceIndex === -1 || choices.some(c => !c.choiceText.trim())) {
        alert('질문, 설명, 최소 5개 보기, 정답 설정을 모두 확인해주세요.');
        return;
    }

    axios.post('/api/teacher/regist', {
        question: question,
        description: description,
        difficulty: difficulty,
        choices: choices
    })
        .then(response => {
            alert('문제가 성공적으로 등록되었습니다.');
            location.reload();
        })
        .catch(error => {
            alert('등록에 실패했습니다. 다시 시도해주세요.');
            console.error(error);
        });
});
