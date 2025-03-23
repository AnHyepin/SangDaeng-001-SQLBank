function searchProblems(){
    let type = document.getElementById("type").value;
    let keyword = document.getElementById("searchKeyword").value;

    if (!keyword.trim()) {
        alert("검색어를 입력하세요!");
        return;
    }

    const role = document.getElementById("role").value;
    const problemListContainer = document.getElementById("problem_list_container");
    problemListContainer.innerHTML = '';

    axios.get(`/api/common/search/problem-list/${type}/${encodeURIComponent(keyword)}`)
        .then(response => {
            const problems = response.data;
            if (problems.length === 0) {
                problemListContainer.innerHTML = '<div class="no-results">검색 결과가 없습니다.</div>';
                return;
            }
            problems.forEach(problem => appendProblem(problem, problemListContainer, role));
        })
        .catch(error => {
            console.error("검색 실패:", error);
            problemListContainer.innerHTML = '<div class="error">검색 중 오류가 발생했습니다.</div>';
        });
}