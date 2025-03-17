document.addEventListener("DOMContentLoaded", function () {
    loadPostDetail();

    // ✅ 수정 버튼 클릭 이벤트 추가
    document.getElementById("updateBtn").addEventListener("click", function () {
        updatePost();
    });

    // ✅ 삭제 버튼 클릭 이벤트 추가
    document.getElementById("deleteBtn").addEventListener("click", function () {
        deletePost();
    });
});

// ✅ 게시글 상세 정보 불러오기
async function loadPostDetail() {
    try {
        console.log("📌 `loadPostDetail()` 실행됨 ✅");

        const postIdElement = document.getElementById("post_id");
        const userIdElement = document.getElementById("user_id"); // 현재 로그인한 사용자 ID
        const roleElement = document.getElementById("role"); // 현재 로그인한 사용자 역할

        if (!postIdElement || !userIdElement || !roleElement) {
            console.error("🚨 필수 요소가 없습니다! (postId, userId, role)");
            return;
        }

        const postId = postIdElement.value;
        const userId = userIdElement.value;
        const role = roleElement.value;

        // console.log("📌 [디버깅] postId:", postId);
        // console.log("📌 [디버깅] userId:", userId);
        // console.log("📌 [디버깅] role:", role);

        if (!postId) {
            console.error("🚨 오류: 게시글 ID가 존재하지 않습니다.");
            return;
        }

        const response = await axios.get(`/api/common/postDetail/${postId}`);
        // console.log("📌 [디버깅] API 응답:", response.data);

        const postData = response.data;
        if (!postData) {
            console.error("🚨 `postData`가 undefined입니다.");
            return;
        }

        // ✅ 카테고리 변환
        const categoryMap = {
            "N": "공지사항",
            "F": "자유게시판",
            "S": "건의사항",
            "Q": "질문"
        };
        const categoryText = categoryMap[postData.category] || "📌 기타";

        // ✅ ID로 HTML 요소 업데이트
        document.getElementById("postId").textContent = postData.postId;
        document.getElementById("createdBy").textContent = postData.createdByName;
        document.getElementById("category").textContent = categoryText;
        document.getElementById("createdAt").textContent = postData.createdAt;
        document.getElementById("description").textContent = postData.title;
        document.getElementById("answer").textContent = postData.content;

        // ✅ ROLE_ADMIN이거나 자신이 작성한 글일 때만 수정 & 삭제 버튼 보이기
        const isOwner = postData.userId.toString() === userId.toString();
        // console.log("📌 [디버깅] 현재 로그인 사용자 ID:", userId);
        // console.log("📌 [디버깅] 게시글 작성자 ID:", postData.userId);
        // console.log("📌 [디버깅] 작성자 본인 여부:", isOwner);

        if (role !== "ROLE_ADMIN" && !isOwner) {
            document.getElementById("updateBtn_box").style.display = "none";
            document.getElementById("deleteBtn_box").style.display = "none";
            document.getElementById("updateBtn_text").style.display = "none";
            document.getElementById("deleteBtn_text").style.display = "none";
        }

    } catch (error) {
        console.error("🚨 게시글 상세 정보를 불러오는 데 실패했습니다:", error);
    }
}

// ✅ 게시글 삭제
async function deletePost() {
    try {
        const postId = document.getElementById("post_id").value;
        if (!confirm("정말로 이 게시글을 삭제하시겠습니까?")) return;

        await axios.delete(`/api/common/delete/${postId}`);
        alert("✅ 게시글이 삭제되었습니다.");
        window.location.href = "/view/common/postList"; // 게시글 목록으로 이동

    } catch (error) {
        alert("🚨 게시글 삭제에 실패했습니다.");
        console.error(error);
    }
}

// ✅ 게시글 수정 페이지로 이동
async function updatePost() {
    const postId = document.getElementById("post_id").value;
    if (!confirm("수정 페이지로 이동하시겠습니까?")) return;
    window.location.href = `/view/common/postUpdate/${postId}`;
}
