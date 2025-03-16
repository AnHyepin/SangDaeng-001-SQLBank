document.addEventListener("DOMContentLoaded", function () {
    document.querySelector(".sign-in-btn").addEventListener("click", function (event) {
        let form = document.getElementById("loginForm");
        let inputs = form.querySelectorAll("input[type='text']");
        let emptyField = null; // 비어 있는 필드를 저장할 변수

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

        const formData = new FormData(form);

        axios.post('/api/common/login', formData, { withCredentials: true })
            .then(response => {
                alert(response.data.message);
                window.location.href = "/";
            })
            .catch(error => {
                if (error.response) {
                    alert(error.response.data); // 400 Bad Request
                } else {
                    alert('로그인에 실패했습니다. 다시 시도해주세요.');
                }
                console.error(error);
            });
    });
});
