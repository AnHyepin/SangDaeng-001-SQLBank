// ✅ postList.js
document.addEventListener("DOMContentLoaded", () => {
    const category = document.getElementById("category").value;

    if (category === "F") {
        document.getElementById("tab_free").classList.add("active-tab");
        document.getElementById("tab_suggestion").classList.add("inactive-tab");
        document.getElementById("tab_qna").classList.add("inactive-tab");
    } else if (category === "S") {
        document.getElementById("tab_suggestion").classList.add("active-tab");
        document.getElementById("tab_free").classList.add("inactive-tab");
        document.getElementById("tab_qna").classList.add("inactive-tab");

    } else if (category === "Q") {
        document.getElementById("tab_qna").classList.add("active-tab");
        document.getElementById("tab_free").classList.add("inactive-tab");
        document.getElementById("tab_suggestion").classList.add("inactive-tab");

    }
});

let currentPage = 1;

// 페이지 로드 시 실행
document.addEventListener("DOMContentLoaded", function () {


    loadPostList();

    // 페이지 크기 선택 시 다시 불러오기
    document.getElementById("pageSizeSelector").addEventListener("change", () => {
        currentPage = 1;
        loadPostList();
    });
});

function loadPostList(page = 1) {
    const category = document.getElementById("category").value;
    const role = document.getElementById("role").value;
    const size = document.getElementById("pageSizeSelector").value;
    const postListContainer = document.getElementById('post_list_container');
    const paginationContainer = document.getElementById('pagination_container');

    postListContainer.innerHTML = '';
    paginationContainer.innerHTML = '';

    // 공지사항
    axios.get(`/api/common/noticeList`)
        .then(response => {
            response.data.forEach(post => appendPost(post, postListContainer, role, true));

            // 일반 게시글 페이징 요청
            return axios.get(`/api/common/postList?category=${category}&page=${page}&size=${size}`);
        })
        .then(response => {
            const { posts, totalPages } = response.data;
            posts.forEach(post => appendPost(post, postListContainer, role, false));
            renderPagination(page, totalPages);
        })
        .catch(error => {
            console.error("게시글 목록을 불러오는 데 실패했습니다.", error);
        });
}

function appendPost(post, container, role, isNotice) {
    const postDiv = document.createElement('div');
    postDiv.className = isNotice ? 'post notice' : 'post';
    const showDelete = (role === "ROLE_ADMIN") ? "" : "hidden-delete";
    const classNumText = post.classNum ? `${post.classNum}기&nbsp;` : '';

    postDiv.innerHTML = `
        <span class="post_id post_text">${post.postId}</span>
        <span class="post_title post_text link">${post.title}</span>
        <span class="post_created_by post_text">${classNumText}${post.createdByName}</span>
        <span class="post_created_at post_text">${post.createdAt}</span>
        <div class="post_delete_btn_box ${showDelete}">
            <button class="post_delete_btn" data-id="${post.postId}">삭제</button>
        </div>
    `;

    postDiv.querySelector('.post_title').addEventListener('click', () => {
        window.location.href = `/view/common/postDetail/${post.postId}`;
    });

    if (role === "ROLE_ADMIN") {
        const deleteButton = postDiv.querySelector('.post_delete_btn');
        deleteButton?.addEventListener('click', () => deletePost(post.postId));
    }

    container.appendChild(postDiv);
}

function deletePost(postId) {
    if (!confirm("정말로 이 게시글을 삭제하시겠습니까?")) return;

    axios.delete(`/api/common/post/${postId}`)
        .then(() => {
            alert("게시글이 삭제되었습니다.");
            loadPostList(currentPage);
        })
        .catch(error => {
            alert("삭제에 실패했습니다.");
            console.error(error);
        });
}

function renderPagination(page, totalPages) {
    const paginationContainer = document.getElementById("pagination_container");

    let html = '';

    if (page > 1) {
        html += `<button class="page-btn prev-btn" onclick="loadPostList(${page - 1})">이전</button>`;
    }

    for (let i = 1; i <= totalPages; i++) {
        html += `<button class="page-btn ${i === page ? 'active' : ''}" onclick="loadPostList(${i})">${i}</button>`;
    }

    if (page < totalPages) {
        html += `<button class="page-btn next-btn" onclick="loadPostList(${page + 1})">다음</button>`;
    }

    paginationContainer.innerHTML = html;
}

