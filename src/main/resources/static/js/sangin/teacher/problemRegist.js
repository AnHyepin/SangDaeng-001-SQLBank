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

document.getElementById('main_choice_section').addEventListener('change', function(e) {
    if (e.target.name === 'correctChoice') {
        document.querySelectorAll('.choice_item label span').forEach(span => {
            span.textContent = span.textContent.replace(' - 정답', '');
        });

        const selectedSpan = e.target.nextElementSibling;
        selectedSpan.textContent += ' - 정답';
    }
});
