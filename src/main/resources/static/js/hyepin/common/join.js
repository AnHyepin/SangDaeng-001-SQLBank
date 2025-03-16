// document.addEventListener("DOMContentLoaded", function () {
//     document.querySelector(".sign-up-btn").addEventListener("click", function (event) {
//         event.preventDefault();
//
//         let form = document.getElementById("joinForm");
//         let inputs = form.querySelectorAll("input[type='text']");
//         let emptyField = null; // 비어 있는 필드를 저장할 변수
//         let password = document.getElementById("password")
//         let password2 = document.getElementById("password2")
//
//         for (let input of inputs) {
//             if (input.value.trim() === "") {
//                 emptyField = input; // 비어 있는 필드 저장
//                 break;
//             }
//         }
//
//         if (emptyField) {
//             alert("모든 값을 입력하세요!");
//             emptyField.focus(); // 가장 먼저 비어 있는 필드에 포커스
//             return;
//         }
//
//         // 비밀번호 검증
//         if (password.value !== password2.value) {
//             alert("비밀번호가 일치하지 않습니다!");
//             password.focus();
//             return;
//         }
//
//         const formData = new FormData(form);
//
//         axios.post('/api/common/join', formData)
//             .then(response => {
//                 alert(response.data);
//                 window.location.href = "/view/common/login";
//             })
//             .catch(error => {
//                 if (error.response) {
//                     alert(error.response.data); // 400 Bad Request
//                 } else {
//                     alert('등록에 실패했습니다. 다시 시도해주세요.');
//                 }
//                 console.error(error);
//             });
//     });
// });

document.addEventListener("DOMContentLoaded", function () {
    document.querySelector(".sign-up-btn").addEventListener("click", function (event) {
        event.preventDefault();

        let form = document.getElementById("joinForm");
        const usernameInput = form.querySelector("input[name='username']");
        const nameInput = form.querySelector("input[name='name']");
        const passwordInput = form.querySelector("input[name='password']");
        const password2Input = form.querySelector("input[name='password2']");
        const emailInput = form.querySelector("input[name='email']");

        function validateForm() {
            let isValid = true;
            let errorMessage = "";

            // ID 검사
            const usernamePattern = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z\d]{8,}$/;
            if (!usernameInput.value.trim()) {
                errorMessage += "⚠️ ID를 입력하세요.\n";
                isValid = false;
            } else if (!usernamePattern.test(usernameInput.value)) {
                errorMessage += "⚠️ ID는 8자 이상이며, 영어와 숫자를 포함해야 합니다.\n";
                isValid = false;
            }

            // 이름 검사
            const namePattern = /^[a-zA-Z가-힣]+$/;
            if (!nameInput.value.trim()) {
                errorMessage += "⚠️ 이름을 입력하세요.\n";
                isValid = false;
            } else if (!namePattern.test(nameInput.value)) {
                errorMessage += "⚠️ 이름은 숫자를 포함할 수 없으며, 영어 또는 한글만 입력 가능합니다.\n";
                isValid = false;
            }

            // 비밀번호 검사
            if (!passwordInput.value.trim()) {
                errorMessage += "⚠️ 비밀번호를 입력하세요.\n";
                isValid = false;
            } else if (passwordInput.value.length < 8) {
                errorMessage += "⚠️ 비밀번호는 8자 이상이어야 합니다.\n";
                isValid = false;
            }

            // 비밀번호 확인 검사
            if (passwordInput.value !== password2Input.value) {
                errorMessage += "⚠️ 비밀번호가 일치하지 않습니다.\n";
                isValid = false;
            }

            // 이메일 검사 (정규식 사용)
            const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            if (!emailInput.value.trim()) {
                errorMessage += "⚠️ 이메일을 입력하세요.\n";
                isValid = false;
            } else if (!emailPattern.test(emailInput.value)) {
                errorMessage += "⚠️ 올바른 이메일 형식이 아닙니다.\n";
                isValid = false;
            }

            return { isValid, errorMessage };
        }

        // 유효성 검사 실행
        const { isValid, errorMessage } = validateForm();

        if (isValid) {
            const formData = new FormData(form);
            axios.post('/api/common/join', formData)
                .then(response => {
                    alert(response.data.message);
                    window.location.href = "/view/common/login";
                })
                .catch(error => {
                    if (error.response) {
                        alert(error.response.data.message); // 400 Bad Request
                    } else {
                        alert('등록에 실패했습니다. 다시 시도해주세요.');
                    }
                    console.error(error);
                });
            } else {
                alert(errorMessage);
            }

    });
});