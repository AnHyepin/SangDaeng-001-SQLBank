document.getElementById('regist_btn').addEventListener('click', function () {
    const problemId = document.getElementById('problem_id').value; // 문제 ID 가져오기
    const question = document.querySelector('[name="question"]').value.trim();
    const description = document.querySelector('[name="description"]').value.trim();
    const difficulty = document.getElementById('main_info_difficulty').value;

    const choiceItems = document.querySelectorAll('.choice_item');
    const choices = [];

    const correctChoiceRadio = document.querySelector('input[name="correctChoice"]:checked');
    const correctChoiceIndex = correctChoiceRadio ? parseInt(correctChoiceRadio.value) : -1;

    choiceItems.forEach((item, index) => {
        const choiceText = item.querySelector('.main_choice_input').value.trim();
        choices.push({
            choiceText: choiceText,
            isCorrect: (index + 1 === correctChoiceIndex) ? 1 : 0
        });
    });

    // 유효성 검사
    if (!problemId || !question || !description || choices.length < 5 || correctChoiceIndex === -1 || choices.some(c => !c.choiceText)) {
        alert('질문, 설명, 최소 5개 보기, 정답 설정을 모두 확인해주세요.');
        return;
    }

    // 문제 수정 요청 (PUT)
    axios.put(`/api/teacher/update/${problemId}`, {
        problemId: problemId,
        question: question,
        description: description,
        difficulty: difficulty,
        choices: choices
    })
        .then(response => {
            alert('문제가 성공적으로 수정되었습니다.');
            location.reload();
        })
        .catch(error => {
            alert('수정에 실패했습니다. 다시 시도해주세요.');
            console.error(error);
        });
});
