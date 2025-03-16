let formIdValue = "find-id";

function handleTabClick(formId, selectedTab) {
    let form = document.getElementById(formId);
    let valueField = form.querySelector(".value-field");
    valueField.innerHTML = "";

    formIdValue = formId;

    document.querySelectorAll(".form-content").forEach(form => {
        form.classList.add("hidden");
    });

    document.getElementById(formId).classList.remove("hidden");

    document.querySelectorAll(".tab").forEach(tab => {
        tab.classList.remove("active");
    });
    selectedTab.classList.add("active");
}

document.querySelector(".find-id-pw-btn").addEventListener("click", function (event) {
    event.preventDefault(); // 기본 이벤트 방지 (필수!)

    let form = document.getElementById(formIdValue);
    let formData = new FormData(form);
    let valueField = form.querySelector(".value-field");

    axios.post(`/api/common/${formIdValue}`, formData)
        .then(response => {
            if(response.data === "find"){
                openModal(document.getElementById("username").value);
            }else{
                valueField.innerHTML = response.data; // 서버 응답 표시 (innerHtml → innerHTML 수정)
            }
        })
        .catch(error => {
            valueField.innerHTML = "";
            if (error.response) {
                alert(error.response.data); // 400 Bad Request 메시지 출력
            } else {
                alert('ID/PW 찾기에 실패했습니다. 다시 시도해주세요.');
            }
            console.error(error);
        });
});

//모달 열기
function openModal(username) {
    document.querySelector(".modal-overlay").classList.remove("hidden"); // ✅ 배경 보이게
    document.querySelector(".change-modal").classList.remove("hidden"); // ✅ 모달 보이게
    document.querySelector("#change-pw").classList.remove("hidden"); // ✅ 폼 보이게

    document.getElementById("modal-username").value = username; // username 값 설정
}

// 모달 닫기
function closeModal() {
    document.querySelector(".modal-overlay").classList.add("hidden"); // ✅ 배경 숨김
    document.querySelector(".change-modal").classList.add("hidden"); // ✅ 모달 숨김
    document.querySelector("#change-pw").classList.add("hidden"); // ✅ 폼 숨김
}

// 비밀번호 변경 요청
function submitChangePw() {
    let password = document.getElementById("password").value.trim();
    let password2 = document.getElementById("password2").value.trim();

    if (password === "" || password2 === "") {
        alert("비밀번호를 입력해주세요!");
        return;
    }

    if (password !== password2) {
        alert("비밀번호가 일치하지 않습니다!");
        return;
    }

    const changeForm = document.getElementById("change-pw");
    let formData = new FormData(changeForm);

    axios.post("/api/common/change-pw", formData)
        .then(response => {
            alert(response.data); // 서버 응답 출력
            closeModal(); // 모달 닫기
        })
        .catch(error => {
            if (error.response) {
                alert(error.response.data);
            } else {
                alert("비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
            }
            console.error(error);
        });
}
