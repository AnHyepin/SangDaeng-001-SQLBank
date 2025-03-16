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
        const postId = document.getElementById("post_id").value;

        if (!postId) {
            console.error("🚨 오류: 게시글 ID가 존재하지 않습니다.");
            return;
        }

        const response = await axios.get(`/api/common/postDetail/${postId}`);
        const postData = response.data;

        // ✅ 카테고리 변환 (N -> 공지사항, F -> 자유게시판, S -> 건의사항, Q -> 질문)
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
        document.getElementById("category").textContent = categoryText; // 변환된 값 적용
        document.getElementById("createdAt").textContent = postData.createdAt;
        document.getElementById("description").textContent = postData.title;
        document.getElementById("answer").textContent = postData.content;

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
