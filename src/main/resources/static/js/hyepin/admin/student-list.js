// 페이지네이션 상태
let currentPage = 1; // 현재 페이지
const itemsPerPage = 10; // 한 페이지당 아이템 수
let totalPages = 1; // 전체 페이지 수 (서버에서 받아올 예정)

// 페이지네이션 생성 함수
function renderPagination() {
    const pageNumbers = document.getElementById("pageNumbers");
    pageNumbers.innerHTML = ""; // 기존 버튼 초기화

    for (let i = 1; i <= totalPages; i++) {
        const button = document.createElement("button");
        button.textContent = i;
        button.classList.add("page-button");

        // 현재 페이지 강조
        if (i === currentPage) {
            button.classList.add("active");
        }

        button.addEventListener("click", function () {
            if (currentPage !== i) {
                currentPage = i;
                getStudentList(currentPage);
            }
        });

        pageNumbers.appendChild(button);
    }

    updateActivePage();
}

// 클릭한 버튼에만 'active' 스타일 적용 + 이전/다음 버튼 숨김 처리
function updateActivePage() {
    document.querySelectorAll(".page-button").forEach(button => {
        button.classList.remove("active");
        if (parseInt(button.textContent) === currentPage) {
            button.classList.add("active");
        }
    });

    // 이전 버튼 숨김 처리
    if (currentPage === 1) {
        document.getElementById("prevPageBtn").classList.add("hidden");
    } else {
        document.getElementById("prevPageBtn").classList.remove("hidden");
    }

    // 다음 버튼 숨김 처리
    if (currentPage === totalPages) {
        document.getElementById("nextPageBtn").classList.add("hidden");
    } else {
        document.getElementById("nextPageBtn").classList.remove("hidden");
    }
}

// 이전 페이지 버튼
document.getElementById("prevPageBtn").addEventListener("click", function () {
    if (currentPage > 1) {
        currentPage--;
        getStudentList(currentPage);
    }
});

// 다음 페이지 버튼
document.getElementById("nextPageBtn").addEventListener("click", function () {
    if (currentPage < totalPages) {
        currentPage++;
        getStudentList(currentPage);
    }
});

//기수별 학생 조회
function getStudentListByClassNum() {
    let classNum = document.getElementById("classNumSelect").value;
    if (!classNum) {
        alert("기수를 입력하세요!");
        return;
    }

    // 숫자인지 확인 (정수만 허용)
    if (!/^\d+$/.test(classNum)) {
        alert("숫자만 입력하세요!");
        return;
    }

    console.log("선택한 기수:", classNum);

    axios.get(`/api/admin/students/by-class?classNum=${classNum}`)
        .then(response => {
            console.log("학생 리스트:", response.data);
            renderTable(response.data);
            document.getElementById("pagination").classList.add("hidden");
        })
        .catch(error => {
            console.error("데이터 가져오기 실패:", error);
        });
}

// 전체 학생 조회 (페이지네이션 포함)
function getStudentList(page = 1) {
    axios.get(`/api/admin/students?page=${page - 1}&size=${itemsPerPage}`)
        .then(response => {
            console.log("학생 리스트:", response.data);
            renderTable(response.data.content);
            document.getElementById("pagination").classList.remove("hidden");

            totalPages = response.data.totalPages; // 전체 페이지 수 업데이트
            currentPage = page; // 현재 페이지 설정
            renderPagination(); // 페이지네이션 다시 그림
        })
        .catch(error => {
            console.error("데이터 가져오기 실패:", error);
        });
}

// 페이지 로드 시 실행
getStudentList();

// 테이블 데이터 렌더링
function renderTable(data) {
    const tbody = document.querySelector('table tbody');
    tbody.innerHTML = ''; // 기존 데이터를 초기화

    data.forEach(user => {
        const tr = document.createElement('tr');

        // 번호
        const tdUserId = document.createElement('td');
        tdUserId.textContent = user.userId;
        tr.appendChild(tdUserId);

        // 기수
        const tdClassNum = document.createElement('td');
        tdClassNum.textContent = `제${user.classNum}기`;
        tr.appendChild(tdClassNum);

        // 이름
        const tdName = document.createElement('td');
        tdName.textContent = user.name;
        tr.appendChild(tdName);

        // ID
        const tdUsername = document.createElement('td');
        tdUsername.textContent = user.username;
        tr.appendChild(tdUsername);

        // 가입일자
        const tdCreatedAt = document.createElement('td');
        tdCreatedAt.textContent = user.createdAt.split('T')[0]; // 날짜만 표시
        tr.appendChild(tdCreatedAt);

        // 권한 변경
        const tdRoleSelect = document.createElement('td');
        const select = document.createElement('select');
        select.innerHTML = `
                    <option value="ROLE_STUDENT" ${user.role === 'ROLE_STUDENT' ? 'selected' : ''}>학생</option>
                    <option value="ROLE_TEACHER" ${user.role === 'ROLE_TEACHER' ? 'selected' : ''}>교사</option>
                `;
        select.id = `role-select-${user.userId}`; // 각 select에 고유 id 추가
        tdRoleSelect.appendChild(select);
        tr.appendChild(tdRoleSelect);

        // 관리 버튼
        const tdManage = document.createElement('td');
        const button = document.createElement('input');
        button.className = 'styled-button';
        button.type = 'button';
        button.value = '확인';
        button.dataset.userId = user.userId; // userId를 data 속성으로 추가
        button.onclick = function () {
            authoritySubmit(this);
        };
        tdManage.appendChild(button);
        tr.appendChild(tdManage);

        // 테이블에 행 추가
        tbody.appendChild(tr);
    });
}

// 권한 변경 버튼
function authoritySubmit(buttonElement) {
    const userId = buttonElement.dataset.userId;
    const selectId = `role-select-${userId}`;
    const selectedRole = document.getElementById(selectId).value;
    const data = {userId, role: selectedRole};
    axios.post('/api/admin/authority', data)
        .then(response => {
            alert(response.data.message)
        })
        .catch(error => {
            alert('서버오류!');
            console.error(error);
        });

}
renderTable(data);