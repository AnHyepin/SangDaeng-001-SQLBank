document.addEventListener("DOMContentLoaded", () => {
    const modalBackground = document.getElementById("comment_modal_background");
    const modal = document.getElementById("comment_modal");
    const openModalBtn = document.getElementById("comment_register_btn");
    const closeModalBtn = document.getElementById("comment_modal_close");
    const cancelBtn = document.getElementById("comment_cancel_btn");
    const submitBtn = document.getElementById("comment_submit_btn");
    const commentInput = document.getElementById("comment_input");

    // 문제 ID 가져오기
    const problemId = document.getElementById("problem_id").value;

    // 모달 열기
    openModalBtn.addEventListener("click", () => {
        modalBackground.classList.add("active");
        modal.classList.add("active");
        commentInput.value = ""; // 입력창 초기화
    });

    // 모달 닫기
    function closeModal() {
        modalBackground.classList.remove("active");
        modal.classList.remove("active");
    }

    closeModalBtn.addEventListener("click", closeModal);
    cancelBtn.addEventListener("click", closeModal);

    // 배경 클릭 시 모달 닫기
    modalBackground.addEventListener("click", (e) => {
        if (e.target === modalBackground) closeModal();
    });

    // 댓글 등록 요청
    submitBtn.addEventListener("click", async () => {
        const commentText = commentInput.value.trim();

        if (commentText === "") {
            alert("댓글을 입력하세요.");
            return;
        }

        try {
            const response = await axios.post("/api/common/discussion", {
                problemId: problemId,  // 문제 ID 포함
                content: commentText,
            });

            if (response.status === 200 || response.status === 201) {
                alert("댓글이 등록되었습니다.");
                closeModal();
                loadDiscussions(); // 댓글 목록 다시 불러오기
            } else {
                alert("댓글 등록에 실패했습니다.");
            }
        } catch (error) {
            console.error("댓글 등록 중 오류 발생:", error);
            alert("댓글 등록 중 오류가 발생했습니다.");
        }
    });

    // 댓글 목록 불러오기
    async function loadDiscussions() {
        try {
            const response = await axios.get(`/api/common/discussionList?problemId=${problemId}`);
            const commentList = document.getElementById("comment_list");
            commentList.innerHTML = ""; // 기존 목록 초기화

            response.data.forEach(comment => {
                const commentItem = document.createElement("div");
                commentItem.classList.add("comment_item");

                commentItem.innerHTML = `
                    <div class="comment_user_id">${comment.userId}</div>
                    <div class="comment_created_at">${new Date(comment.createdAt).toLocaleString()}</div>
                    <div class="comment_content">${comment.content}</div>
                    <button class="comment_delete_btn" data-id="${comment.discussionId}">삭제</button>
                `;

                commentList.appendChild(commentItem);
            });

            // 삭제 버튼 이벤트 리스너 추가
            document.querySelectorAll(".comment_delete_btn").forEach(btn => {
                btn.addEventListener("click", async (e) => {
                    const discussionId = e.target.dataset.id;
                    await deleteDiscussion(discussionId);
                });
            });

        } catch (error) {
            console.error("댓글 목록 불러오기 실패:", error);
        }
    }

    // 댓글 삭제 기능
    async function deleteDiscussion(discussionId) {
        if (!confirm("댓글을 삭제하시겠습니까?")) return;

        try {
            const response = await axios.delete(`/api/teacher/discussion/${discussionId}`);

            if (response.status === 200) {
                alert("댓글이 삭제되었습니다.");
                loadDiscussions(); // 삭제 후 다시 로드
            } else {
                alert("댓글 삭제에 실패했습니다.");
            }
        } catch (error) {
            console.error("댓글 삭제 중 오류 발생:", error);
            alert("댓글 삭제 중 오류가 발생했습니다.");
        }
    }

    // 페이지 로드 시 댓글 목록 가져오기
    loadDiscussions();
});
