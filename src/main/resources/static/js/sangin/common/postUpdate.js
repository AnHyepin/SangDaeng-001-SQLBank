document.addEventListener("DOMContentLoaded", function () {
    loadPostDetail();

    // ✅ 게시글 수정 버튼 클릭 이벤트 추가
    document.getElementById("post_regist_btn").addEventListener("click", function () {
        updatePost();
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

        // ✅ 카테고리 변환 (N -> 공지사항, F -> 자유게시판, S -> 건의사항)
        const categoryMap = {
            "N": "공지사항",
            "F": "자유게시판",
            "S": "건의사항"
        };
        const categoryText = categoryMap[postData.category] || "기타";

        // ✅ ID로 HTML 요소 업데이트
        document.getElementById("title").value = postData.title;
        document.getElementById("category").value = postData.category; // 변환된 값 적용
        document.getElementById("content").value = postData.content;

    } catch (error) {
        console.error("🚨 게시글 상세 정보를 불러오는 데 실패했습니다:", error);
        alert("게시글 정보를 불러오는 데 실패했습니다.");
    }
}

// ✅ 게시글 수정 요청
async function updatePost() {
    try {
        const postId = document.getElementById("post_id").value;
        const title = document.getElementById("title").value.trim();
        const category = document.getElementById("category").value;
        const content = document.getElementById("content").value.trim();

        // ✅ 유효성 검사
        if (!title || !content) {
            alert("🚨 제목과 내용을 입력해 주십시오.");
            return;
        }

        const response = await axios.put(`/api/common/post/${postId}`, {
            title: title,
            category: category,
            content: content
        });

        if (response.status === 200) {
            alert("✅ 게시글이 수정되었습니다.");
            window.location.href = `/view/common/postDetail/${postId}`; // 수정 후 상세 페이지 이동
        } else {
            alert("🚨 게시글 수정에 실패했습니다.");
        }

    } catch (error) {
        console.error("🚨 게시글 수정 중 오류 발생:", error);
        alert("게시글 수정 중 오류가 발생했습니다.");
    }
}
