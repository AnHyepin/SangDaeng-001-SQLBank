document.getElementById('add_choice_btn').addEventListener('click', function () {
    const choiceSection = document.getElementById('main_choice_section');
    const choiceCount = choiceSection.querySelectorAll('.main_choice_text').length + 1;

    const choiceDiv = document.createElement('div');
    choiceDiv.innerHTML = `
        <div class="main_choice_text">보기 ${choiceCount}</div>
        <textarea class="main_choice_input"></textarea>
    `;
    choiceSection.appendChild(choiceDiv);
});