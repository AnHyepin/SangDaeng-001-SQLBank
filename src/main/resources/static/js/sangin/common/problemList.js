let currentPage = 1;

document.addEventListener("DOMContentLoaded", function () {
    loadProblemList();

    // ✅ 페이지 크기 선택 시 다시 불러오기
    document.getElementById("pageSizeSelector").addEventListener("change", () => {
        currentPage = 1;
        loadProblemList();
    });
});

function loadProblemList(page = 1) {
    const role = document.getElementById("role").value;
    const size = document.getElementById("pageSizeSelector").value;
    const problemListContainer = document.getElementById("problem_list_container");
    const paginationContainer = document.getElementById("pagination_container");

    problemListContainer.innerHTML = '';
    paginationContainer.innerHTML = '';

    axios.get(`/api/common/problemList?page=${page}&size=${size}`)
        .then(response => {
            const { problems, totalPages } = response.data;
            problems.forEach(problem => appendProblem(problem, problemListContainer, role));
            renderPagination(page, totalPages);
        })
        .catch(error => {
            console.error("문제 목록을 불러오는 데 실패했습니다.", error);
        });
}

function appendProblem(problem, container, role) {
    const problemDiv = document.createElement('div');
    problemDiv.className = 'problem';

    const statusText = problem.permitYn === 'Y' ? '활성화' : '비활성화';
    const statusClass = problem.permitYn === 'Y' ? 'active' : 'inactive';
    const classNumText = problem.classNum ? `${problem.classNum}기&nbsp;` : '';

    problemDiv.innerHTML = `
        <span class="problem_id problem_text">${problem.problemId}</span>
        <span class="problem_question problem_text link">${problem.question}</span>
        <span class="problem_created_by problem_text">${classNumText}${problem.createdByName}</span>
        <span class="problem_created_at problem_text">${problem.createdAt}</span>
        <div class="problem_status_box role_teacher">
            <span class="problem_status ${statusClass}" data-id="${problem.problemId}" data-status="${problem.permitYn}">${statusText}</span>
        </div>
        <div class="problem_delete_btn_box role_admin">
            <button class="problem_delete_btn" data-id="${problem.problemId}">삭제</button>
        </div>
    `;

    problemDiv.querySelector('.problem_question').addEventListener('click', function () {
        window.location.href = `/view/common/problemDetail/${problem.problemId}`;
    });

    const statusElement = problemDiv.querySelector('.problem_status');
    if (statusElement) {
        statusElement.addEventListener('click', function () {
            toggleProblemStatus(problem.problemId, this);
        });
    }

    const deleteButton = problemDiv.querySelector('.problem_delete_btn');
    if (deleteButton) {
        deleteButton.addEventListener('click', function () {
            deleteProblem(problem.problemId);
        });
    }

    container.appendChild(problemDiv);
}

function renderPagination(page, totalPages) {
    const paginationContainer = document.getElementById("pagination_container");

    let html = '';
    if (page > 1) {
        html += `<button class="page-btn prev-btn" onclick="loadProblemList(${page - 1})">이전</button>`;
    }

    for (let i = 1; i <= totalPages; i++) {
        html += `<button class="page-btn ${i === page ? 'active' : ''}" onclick="loadProblemList(${i})">${i}</button>`;
    }

    if (page < totalPages) {
        html += `<button class="page-btn next-btn" onclick="loadProblemList(${page + 1})">다음</button>`;
    }

    paginationContainer.innerHTML = html;
}


// ✅ 문제 상태 변경 (활성화 ↔ 비활성화)
function toggleProblemStatus(problemId, statusElement) {
    const currentStatus = statusElement.dataset.status;
    const newStatus = currentStatus === 'Y' ? 'N' : 'Y';

    axios.put(`/api/teacher/updateStatus/${problemId}`, {permitYn: newStatus})
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
