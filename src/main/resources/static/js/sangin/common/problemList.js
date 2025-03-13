document.addEventListener("DOMContentLoaded", function () {
    loadProblemList();
});

function loadProblemList() {
    axios.get('/api/common/problemList')
        .then(response => {
            const problemListContainer = document.getElementById('problem_list_container');
            problemListContainer.innerHTML = ''; // 기존 목록 초기화

            response.data.forEach(problem => {
                const problemDiv = document.createElement('div');
                problemDiv.className = 'problem';

                // 문제 상태 설정 (permit_yn이 'Y'이면 활성화, 'N'이면 비활성화)
                const statusText = problem.permitYn === 'Y' ? '활성화' : '비활성화';
                const statusClass = problem.permitYn === 'Y' ? 'active' : 'inactive';

                problemDiv.innerHTML = `
                    <span class="problem_id problem_text">${problem.problemId}</span>
                    <span class="problem_question problem_text link">${problem.question}</span>
                    <span class="problem_created_by problem_text">${problem.createdBy}</span>
                    <span class="problem_created_at problem_text">${problem.createdAt}</span>
                    <div class="problem_status_box">
                        <span class="problem_status ${statusClass}" data-id="${problem.problemId}" data-status="${problem.permitYn}">${statusText}</span>
                    </div>
                    <div class="problem_delete_btn_box" >
                        <button class="problem_delete_btn" data-id="${problem.problemId}">삭제</button>
                    </div>
                `;

                // 문제 상세 페이지 이동 이벤트 추가
                problemDiv.querySelector('.problem_question').addEventListener('click', function () {
                    window.location.href = `/view/common/problemDetail/${problem.problemId}`;
                });

                // 상태 변경 (활성화 <-> 비활성화)
                problemDiv.querySelector('.problem_status').addEventListener('click', function () {
                    toggleProblemStatus(problem.problemId, this);
                });

                // 문제 삭제 버튼 클릭 이벤트 추가
                problemDiv.querySelector('.problem_delete_btn').addEventListener('click', function () {
                    deleteProblem(problem.problemId);
                });

                problemListContainer.appendChild(problemDiv);
            });
        })
        .catch(error => {
            console.error("문제 목록을 불러오는 데 실패했습니다.", error);
        });
}

// ✅ 문제 상태 변경 (활성화 ↔ 비활성화)
function toggleProblemStatus(problemId, statusElement) {
    const currentStatus = statusElement.dataset.status;
    const newStatus = currentStatus === 'Y' ? 'N' : 'Y';

    axios.put(`/api/teacher/updateStatus/${problemId}`, { permitYn: newStatus })
        .then(response => {
            statusElement.dataset.status = newStatus;
            statusElement.textContent = newStatus === 'Y' ? '활성화' : '비활성화';
            statusElement.classList.toggle('active', newStatus === 'Y');
            statusElement.classList.toggle('inactive', newStatus === 'N');
        })
        .catch(error => {
            alert("상태 변경에 실패했습니다.");
            console.error(error);
        });
}

// ✅ 문제 삭제
function deleteProblem(problemId) {
    if (!confirm("정말로 이 문제를 삭제하시겠습니까?")) return;

    axios.delete(`/api/teacher/problem/${problemId}`)
        .then(response => {
            alert("문제가 삭제되었습니다.");
            loadProblemList(); // 삭제 후 리스트 갱신
        })
        .catch(error => {
            alert("삭제에 실패했습니다.");
            console.error(error);
        });
}
