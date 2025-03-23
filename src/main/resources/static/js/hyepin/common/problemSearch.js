// function searchProblems(){
//     let type = document.getElementById("type").value;
//     let keyword = document.getElementById("searchKeyword").value;
//
//     if (!keyword.trim()) {
//         alert("검색어를 입력하세요!");
//         return;
//     }
//
//     const role = document.getElementById("role").value;
//     const problemListContainer = document.getElementById("problem_list_container");
//     problemListContainer.innerHTML = '';
//
//     axios.get(`/api/common/search/problem-list/${type}/${encodeURIComponent(keyword)}`)
//         .then(response => {
//             const problems = response.data;
//             if (problems.length === 0) {
//                 problemListContainer.innerHTML = '<div class="no-results">검색 결과가 없습니다.</div>';
//                 return;
//             }
//             problems.forEach(problem => appendProblem(problem, problemListContainer, role));
//         })
//         .catch(error => {
//             console.error("검색 실패:", error);
//             problemListContainer.innerHTML = '<div class="error">검색 중 오류가 발생했습니다.</div>';
//         });
// }

function searchProblems() {
    const type = document.getElementById("type").value;
    const keyword = document.getElementById("searchKeyword").value.trim();
    const size = document.getElementById("pageSizeSelector").value;

    if (type === 'id' && keyword !== '' && isNaN(keyword)) {
        alert("문제 ID는 숫자만 입력 가능합니다.");
        return;
    }

    if (!keyword) {
        alert("검색어를 입력해주세요.");
        return;
    }

    axios.get(`/api/common/search/problemList?page=1&size=${size}&type=${type}&keyword=${keyword}`)
        .then(response => {
            const { problems, totalPages } = response.data;
            const problemListContainer = document.getElementById("problem_list_container");
            problemListContainer.innerHTML = '';

            if (!problems || problems.length === 0) {
                problemListContainer.innerHTML = '<div class="no-results">검색 결과가 없습니다.</div>';
            } else {
                problems.forEach(problem => appendProblem(problem, problemListContainer, role));
                renderPagination(1, totalPages, type, keyword);
            }
        })
        .catch(error => {
            console.error("검색에 실패했습니다.", error);
            const problemListContainer = document.getElementById("problem_list_container");
            problemListContainer.innerHTML = '<div class="error-message">검색에 실패했습니다.</div>';
        });
}