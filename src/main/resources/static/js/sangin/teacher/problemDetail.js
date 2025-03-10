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
                const choiceDiv = document.createElement('div');
                choiceDiv.className = 'choice_item';

                const isChecked = choice.isCorrect ? 'checked' : '';

                choiceDiv.innerHTML = `
                    <label>
                        <input type="radio" name="correctChoice" value="${index + 1}" ${isChecked}>
                        <span>보기 ${index + 1}${isChecked ? ' - 정답' : ''}</span>
                    </label>
                    <textarea class="main_choice_input">${choice.choiceText}</textarea>
                `;
                choiceSection.appendChild(choiceDiv);
            });
        })
        .catch(error => {
            console.error("문제 데이터를 불러오는 데 실패했습니다.", error);
        });
}