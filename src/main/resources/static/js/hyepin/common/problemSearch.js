function searchProblems(){

    let type = document.getElementById("type").value;
    let keyword = document.getElementById("searchKeyword").value;

    if (!keyword.trim()) {
        alert("검색어를 입력하세요!");
        return;
    }

    const encodedKeyword = encodeURIComponent(keyword);

    axios.get(`/api/common/search/problem-list/${type}/${encodeURIComponent(keyword)}`)
        .then(response => {
           response.data;
        })
        .catch(error => {
            console.error("학생 리스트 가져오기 실패:", error);
        });
}