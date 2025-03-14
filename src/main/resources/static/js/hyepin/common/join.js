document.addEventListener("DOMContentLoaded", function () {
    document.querySelector(".sign-up-btn").addEventListener("click", function (event) {
        event.preventDefault();

        let form = document.getElementById("joinForm");
        let inputs = form.querySelectorAll("input[type='text']");
        let emptyField = null; // 비어 있는 필드를 저장할 변수
        let password = document.getElementById("password")
        let password2 = document.getElementById("password2")

        for (let input of inputs) {
            if (input.value.trim() === "") {
                emptyField = input; // 비어 있는 필드 저장
                break;
            }
        }

        if (emptyField) {
            alert("모든 값을 입력하세요!");
            emptyField.focus(); // 가장 먼저 비어 있는 필드에 포커스
            return;
        }

        // 비밀번호 검증
        if (password.value !== password2.value) {
            alert("비밀번호가 일치하지 않습니다!");
            password.focus();
            return;
        }

        const formData = new FormData(form);

        axios.post('/api/auth/join', formData)
            .then(response => {
                alert(response.data);
                window.location.href = "/view/common/login";
            })
            .catch(error => {
                if (error.response) {
                    alert(error.response.data); // 400 Bad Request
                } else {
                    alert('등록에 실패했습니다. 다시 시도해주세요.');
                }
                console.error(error);
            });
    });
});
