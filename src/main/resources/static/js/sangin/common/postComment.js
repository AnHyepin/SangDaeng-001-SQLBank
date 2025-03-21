// ✅ 댓글 기능 전체 연결 스크립트

document.addEventListener("DOMContentLoaded", () => {
    const modalBackground = document.getElementById("comment_modal_background");
    const modal = document.getElementById("comment_modal");
    const openModalBtn = document.getElementById("comment_register_btn");
    const closeModalBtn = document.getElementById("comment_modal_close");
    const cancelBtn = document.getElementById("comment_cancel_btn");
    const submitBtn = document.getElementById("comment_submit_btn");
    const commentInput = document.getElementById("comment_input");

    const postId = document.getElementById("post_id").value;
    const userId = document.getElementById("user_id").value;
    const role = document.getElementById("role").value;

    openModalBtn.addEventListener("click", () => {
        modalBackground.classList.add("active");
        modal.classList.add("active");
        commentInput.value = "";
    });

    function closeModal() {
        modalBackground.classList.remove("active");
        modal.classList.remove("active");
    }

    closeModalBtn.addEventListener("click", closeModal);
    cancelBtn.addEventListener("click", closeModal);

    modalBackground.addEventListener("click", (e) => {
        if (e.target === modalBackground) closeModal();
    });

    submitBtn.addEventListener("click", async () => {
        const commentText = commentInput.value.trim();
        if (commentText === "") {
            alert("댓글을 입력하세요.");
            return;
        }

        try {
            const response = await axios.post("/api/common/comment", {
                postId: postId,
                content: commentText,
            });

            if (response.status === 200 || response.status === 201) {
                alert("✅ 댓글이 등록되었습니다.");
                closeModal();
                loadComments();
            } else {
                alert("🚨 댓글 등록에 실패했습니다.");
            }
        } catch (error) {
            console.error("🚨 댓글 등록 중 오류 발생:", error);
            alert("🚨 댓글 등록 중 오류가 발생했습니다.");
        }
    });

    async function loadComments() {
        try {
            const response = await axios.get(`/api/common/commentList?postId=${postId}`);
            const commentList = document.getElementById("comment_list");
            commentList.innerHTML = "";

            response.data.forEach(comment => {
                const commentItem = document.createElement("div");
                commentItem.classList.add("comment_item");

                const showModify = (comment.userId.toString() === userId.toString() || role === "ROLE_ADMIN") ? "" : "hidden-action";
                let classNumText = comment.classNum ? `${comment.classNum}기&nbsp;` : "";

                commentItem.innerHTML = `
                    <div class="comment_user_id">${classNumText}${comment.createdByName}</div>
                    <div class="comment_created_at">${new Date(comment.createdAt).toLocaleString()}</div>
                    <div class="comment_content">${comment.content}</div>
                    <textarea class="comment_edit_input hidden-action">${comment.content}</textarea>
                    <button class="comment_update_btn ${showModify}" data-id="${comment.commentId}">수정</button>
                    <button class="comment_confirm_btn hidden-action" data-id="${comment.commentId}">수정 완료</button>
                    <button class="comment_cancel_edit_btn hidden-action" data-id="${comment.commentId}">취소</button>
                    <button class="comment_delete_btn ${showModify}" data-id="${comment.commentId}">삭제</button>
                `;

                commentList.appendChild(commentItem);
            });

            document.querySelectorAll(".comment_update_btn").forEach(btn => {
                btn.addEventListener("click", (e) => {
                    const commentId = e.target.dataset.id;
                    toggleEditMode(commentId, true);
                });
            });

            document.querySelectorAll(".comment_confirm_btn").forEach(btn => {
                btn.addEventListener("click", async (e) => {
                    const commentId = e.target.dataset.id;
                    await updateComment(commentId);
                });
            });

            document.querySelectorAll(".comment_cancel_edit_btn").forEach(btn => {
                btn.addEventListener("click", (e) => {
                    const commentId = e.target.dataset.id;
                    toggleEditMode(commentId, false);
                });
            });

            document.querySelectorAll(".comment_delete_btn").forEach(btn => {
                btn.addEventListener("click", async (e) => {
                    const commentId = e.target.dataset.id;
                    await deleteComment(commentId);
                });
            });

        } catch (error) {
            console.error("🚨 댓글 목록 불러오기 실패:", error);
        }
    }

    function toggleEditMode(commentId, isEditing) {
        const commentItem = document.querySelector(`.comment_update_btn[data-id="${commentId}"]`).closest(".comment_item");
        const contentDiv = commentItem.querySelector(".comment_content");
        const editInput = commentItem.querySelector(".comment_edit_input");
        const deleteBtn = commentItem.querySelector(".comment_delete_btn");
        const updateBtn = commentItem.querySelector(".comment_update_btn");
        const confirmBtn = commentItem.querySelector(".comment_confirm_btn");
        const cancelBtn = commentItem.querySelector(".comment_cancel_edit_btn");

        if (isEditing) {
            contentDiv.classList.add("hidden-action");
            editInput.classList.remove("hidden-action");
            updateBtn.classList.add("hidden-action");
            deleteBtn.classList.add("hidden-action");
            confirmBtn.classList.remove("hidden-action");
            cancelBtn.classList.remove("hidden-action");
            editInput.focus();
        } else {
            contentDiv.classList.remove("hidden-action");
            deleteBtn.classList.remove("hidden-action");
            editInput.classList.add("hidden-action");
            updateBtn.classList.remove("hidden-action");
            confirmBtn.classList.add("hidden-action");
            cancelBtn.classList.add("hidden-action");
        }
    }

    async function updateComment(commentId) {
        const commentItem = document.querySelector(`.comment_update_btn[data-id="${commentId}"]`).closest(".comment_item");
        const editInput = commentItem.querySelector(".comment_edit_input");
        const newContent = editInput.value.trim();

        if (newContent === "") {
            alert("수정할 내용을 입력하세요.");
            return;
        }

        try {
            const response = await axios.put(`/api/common/comment/${commentId}`, {
                content: newContent
            });

            if (response.status === 200) {
                alert("✅ 댓글이 수정되었습니다.");
                loadComments();
            } else {
                alert("🚨 댓글 수정에 실패했습니다.");
            }
        } catch (error) {
            console.error("🚨 댓글 수정 중 오류 발생:", error);
            alert("🚨 댓글 수정 중 오류가 발생했습니다.");
        }
    }

    async function deleteComment(commentId) {
        if (!confirm("정말로 댓글을 삭제하시겠습니까?")) return;

        try {
            const response = await axios.delete(`/api/common/comment/${commentId}`);

            if (response.status === 200) {
                alert("✅ 댓글이 삭제되었습니다.");
                loadComments();
            } else {
                alert("🚨 댓글 삭제에 실패했습니다.");
            }
        } catch (error) {
            console.error("🚨 댓글 삭제 중 오류 발생:", error);
            alert("🚨 댓글 삭제 중 오류가 발생했습니다.");
        }
    }

    loadComments();
});
