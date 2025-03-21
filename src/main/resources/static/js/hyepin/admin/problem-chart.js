//WrongRateTop10 가져오기
function fetchWrongRateTop10(difficulty) {
    axios.get("/api/admin/wrong-rate?difficulty=" + difficulty)
        .then(response => {
            console.log("fetchWrongRateTop10 데이터 가져오기 성공:", response.data);
            wrongRateChartUpdate(response.data);
        })
        .catch(error => {
            console.error("데이터 가져오기 실패:", error);
        });
}

// 차트 객체 선언 (업데이트 시 필요)
let wrongRateChart;

// WrongRateTop10 차트 업데이트 함수
function wrongRateChartUpdate(data) {
    const ctx1 = document.getElementById('wrongRateChart').getContext('2d');

    // 데이터 가공
    const labels = data.map(item => `문제 ${item.problemId}`); // 문제 ID를 라벨로 사용
    const wrongRates = data.map(item => item.wrongRate); // 오답률 데이터 추출
    const questions = data.map(item => item.question); // 문제 내용 저장 (툴팁용)
    const problemIds = data.map(item => item.problemId); // 문제 ID 저장 (클릭용)

    // 기존 차트가 있으면 업데이트, 없으면 새로 생성
    if (wrongRateChart) {
        wrongRateChart.data.labels = labels;
        wrongRateChart.data.datasets[0].data = wrongRates;
        wrongRateChart.options.plugins.tooltip.callbacks.label = function (tooltipItem) {
            const index = tooltipItem.dataIndex;
            return [`번호: ${labels[index]}`, `오답률: ${wrongRates[index]}%`, `지문: ${questions[index]}`];
        };
        wrongRateChart.update(); // 차트 업데이트
    } else {
        wrongRateChart = new Chart(ctx1, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: '오답률 (%)',
                    data: wrongRates,
                    backgroundColor: 'blue'
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        max: 100                    }

                },
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: function(tooltipItem) {
                                const index = tooltipItem.dataIndex;
                                return [`번호: ${labels[index]}`, `오답률: ${wrongRates[index]}%`, `지문: ${questions[index]}`];
                            }
                        }
                    }
                },
                interaction: { // 차트 클릭이 가능하도록 설정
                    mode: 'nearest',
                    intersect: true
                },
                onClick: (event, elements) => { // 클릭 이벤트 추가
                    if (elements.length > 0) {
                        const index = elements[0].index;
                        const problemId = problemIds[index]; // 클릭한 문제 ID 가져오기
                        window.open(`/view/common/problemDetail/${problemId}`, '_blank');
                    }
                },
                onHover: (event, elements) => { // 마우스 오버 시 커서 변경
                    if (elements.length > 0) {
                        event.native.target.style.cursor = 'pointer'; // 마우스 오버 시 포인터
                    } else {
                        event.native.target.style.cursor = 'default'; // 기본값으로 되돌리기
                    }
                }
            }
        });
    }
}

//난이도별 문제 해결률 분석
function fetchCorrectRate(){
    axios.get("/api/admin/correct-rate")
        .then(response => {
            console.log("fetchCorrectRate 데이터 가져오기 성공:", response.data);
            correctRateChartUpdate(response.data);
        })
        .catch(error => {
            console.error("데이터 가져오기 실패:", error);
        });
}

// 난이도별 문제 해결률 차트 업데이트 함수
function correctRateChartUpdate(data) {
    const ctx2 = document.getElementById('difficultyChart').getContext('2d');

    // 데이터 변환
    const labels = data.map(item => {
        if (item.difficulty === 'EASY') return '쉬움';
        if (item.difficulty === 'MEDIUM') return '보통';
        if (item.difficulty === 'HARD') return '어려움';
        return item.difficulty; // 혹시 다른 값이 올 경우 대비
    });

    const correctRates = data.map(item => item.correctRate); // 정답률 데이터
    // 난이도별 색상 매핑 (쉬움=노랑, 보통=초록, 어려움=빨강)
    const colorMapping = {
        '쉬움': 'yellow',
        '보통': 'green',
        '어려움': 'red'
    };

    const colors = labels.map(label => colorMapping[label]); // 난이도에 맞게 색상 적용

    // 기존 차트 삭제 후 다시 생성 (안 하면 중복됨)
    if (window.correctRateChart) {
        window.correctRateChart.destroy();
    }

    // 새 차트 생성
    window.correctRateChart = new Chart(ctx2, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                label: '해결률 (%)',
                data: correctRates,
                backgroundColor: colors
            }]
        },
        options: {
            responsive: true,
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function(tooltipItem) {
                            const index = tooltipItem.dataIndex;
                            return `${labels[index]}: ${correctRates[index].toFixed(2)}%`;
                        }
                    }
                }
            }
        }
    });
}