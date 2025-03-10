document.addEventListener("DOMContentLoaded", function () {
    document.querySelector(".sign-up-btn").addEventListener("click", function (event) {
        let form = document.getElementById("joinForm");
        let inputs = form.querySelectorAll("input[type='text']");
        let emptyField = null; // 비어 있는 필드를 저장할 변수
        let passwordFields = [];

        for (let input of inputs) {
            if (input.name === "password") {
                passwordFields.push(input); // 비밀번호 필드 저장
            }

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
        if (passwordFields.length === 2 && passwordFields[0].value !== passwordFields[1].value) {
            alert("비밀번호가 일치하지 않습니다!");
            passwordFields[0].focus();
            return;
        }

        const formData = new FormData(form);

        axios.post('/api/login/join', formData)
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
