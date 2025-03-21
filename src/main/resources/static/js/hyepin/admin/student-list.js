
// 페이지네이션 상태
let currentPage = 0; // 현재 페이지
const itemsPerPage = 10; // 한 페이지당 아이템 수

// 이벤트 리스너 등록 코드
document.getElementById("prevPageBtn").addEventListener("click", () => {
    if (currentPage > 0) {
        currentPage -= 1; // 이전 페이지로 이동
        getStudentList(); // 페이지 변경 후 데이터 가져오기
    }
});

document.getElementById("nextPageBtn").addEventListener("click", () => {
    currentPage += 1; // 다음 페이지로 이동
    getStudentList(); // 페이지 변경 후 데이터 가져오기
});

function setPageAndFetch() {
    currentPage = 0; // 현재 페이지를 0으로 초기화
    getStudentList(); // 학생 리스트 조회
}


//기수별 학생 조회
function getStudentListByClassNum(){
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

//학생 전체 조회
function getStudentList(){
    axios.get(`/api/admin/students?page=${currentPage}&size=${itemsPerPage}`)
        .then(response => {
            console.log("학생 리스트:", response.data);
            renderTable(response.data.content);
            document.getElementById("pagination").classList.remove("hidden");
        })
        .catch(error => {
            console.error("데이터 가져오기 실패:", error);
        });

}

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