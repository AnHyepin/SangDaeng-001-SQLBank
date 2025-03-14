document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("post_regist_btn").addEventListener("click", function () {
        const title = document.getElementById("title").value;
        const content = document.getElementById("content").value;
        const category = document.getElementById("category").value;

        if (!title || !content) {
            alert('제목과 내용을 입력해주십시오.');
            return;
        }

        axios.post(`/api/common/post`,
            {
                title: title,
                content: content,
                category : category
            })
            .then(response => {
                const data = response.data;
                console.log(data);
                alert("게시글 등록에 성공했습니다.");
                window.location.href = `/view/common/postDetail/${data}`;
            })
            .catch(error => {
                console.log("게시글 등록 중 오류가 발생했습니다", error);
                alert("게시글 등록에 실패했습니다. 다시 시도해 주세요.");
            })
    })
})