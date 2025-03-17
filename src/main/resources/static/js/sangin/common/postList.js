document.addEventListener("DOMContentLoaded", function () {
    loadPostList();
});

// ✅ 게시글 목록 불러오기
function loadPostList() {
    const category = document.getElementById("category").value;
    const userId = document.getElementById("userId").value; // 현재 로그인한 사용자 ID 가져오기
    const role = document.getElementById("role").value; // 현재 로그인한 사용자 역할 가져오기

    axios.get(`/api/common/postList?category=${category}`)
        .then(response => {
            const postListContainer = document.getElementById('post_list_container');
            postListContainer.innerHTML = ''; // 기존 목록 초기화

            response.data.forEach(post => {
                const postDiv = document.createElement('div');
                postDiv.className = 'post';

                // ✅ ROLE_ADMIN이면 모든 삭제 버튼 표시, 아니라면 자신의 글만 표시
                const isOwner = post.userId.toString() === userId.toString();
                const showDelete = (role === "ROLE_ADMIN" || isOwner) ? "" : "hidden-delete"; // 숨김 클래스 적용

                postDiv.innerHTML = `
                    <span class="post_id post_text">${post.postId}</span>
                    <span class="post_title post_text link">${post.title}</span>
                    <span class="post_created_by post_text">${post.createdByName}</span>
                    <span class="post_created_at post_text">${post.createdAt}</span>
                    <div class="post_delete_btn_box ${showDelete}">
                        <button class="post_delete_btn" data-id="${post.postId}">삭제</button>
                    </div>
                `;

                // 게시글 상세 페이지 이동 이벤트 추가
                postDiv.querySelector('.post_title').addEventListener('click', function () {
                    window.location.href = `/view/common/postDetail/${post.postId}`;
                });

                // 게시글 삭제 버튼 클릭 이벤트 추가
                const deleteButton = postDiv.querySelector('.post_delete_btn');
                if (deleteButton) {
                    deleteButton.addEventListener('click', function () {
                        deletePost(post.postId);
                    });
                }

                postListContainer.appendChild(postDiv);
            });
        })
        .catch(error => {
            console.error("게시글 목록을 불러오는 데 실패했습니다.", error);
        });
}

// ✅ 게시글 삭제
function deletePost(postId) {
    if (!confirm("정말로 이 게시글을 삭제하시겠습니까?")) return;

    axios.delete(`/api/common/post/${postId}`)
        .then(response => {
            alert("게시글이 삭제되었습니다.");
            loadPostList(); // 삭제 후 리스트 갱신
        })
        .catch(error => {
            alert("삭제에 실패했습니다.");
            console.error(error);
        });
}
