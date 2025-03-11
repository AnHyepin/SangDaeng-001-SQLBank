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
        <div class="choice_box1">
            <textarea class="main_choice_input"></textarea>
            <button type="button" class="delete_choice_btn">삭제</button>
        </div>
    `;

    choiceSection.appendChild(choiceDiv);

    // 삭제 버튼 클릭 이벤트 추가
    choiceDiv.querySelector('.delete_choice_btn').addEventListener('click', function () {
        choiceDiv.remove();
        updateChoiceNumbers();
    });
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
