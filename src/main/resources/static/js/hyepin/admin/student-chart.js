let averageScoreData = [];
//학생별 평균 점수 가져오기
function fetchAverageScore() {
    axios.get("/api/admin/average-score")
        .then(response => {
            console.log("fetchAverageScore 데이터 가져오기 성공:", response.data);
            averageScoreData = response.data;
            averageScoreUpdateChart(averageScoreData, "EASY");
        })
        .catch(error => {
            console.error("데이터 가져오기 실패:", error);
        });
}

// 기본은 EASY로 차트를 그리기
// averageScoreUpdateChart("EASY");

const ctx3 = document.getElementById('rankingChart').getContext('2d');
let rankingChart = null;
// 차트 데이터를 업데이트하는 함수
function averageScoreUpdateChart(data, selectedDifficulty) {
    const filteredData = data.filter(item => item.difficulty === selectedDifficulty);
    const userId = filteredData.map(item => item.username);
    const labels = filteredData.map(item => item.name);
    const scores = filteredData.map(item => Number(item.avgScore.toFixed(1)));

    // 기존 차트가 있으면 업데이트, 없으면 새로 생성
    if (rankingChart) {
        rankingChart.data.labels = labels;
        rankingChart.data.datasets[0].data = scores;
        rankingChart.update();
    } else {
        rankingChart = new Chart(ctx3, {
            type: 'bar', // 막대형 차트로 설정
            data: {
                labels: labels,
                datasets: [{
                    label: '학생별 평균 점수',
                    data: scores,
                    borderColor: 'blue',
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        max: 100 // 점수는 최대 100으로 고정
                    }
                }
            }
        });
    }
}

let scoreChangeData = [];
let studentList = [];

//학생별 점수 변화 가져오기
function fetchScoreChange() {
    axios.get("/api/admin/score-change")
        .then(response => {
            console.log("fetchScoreChange 데이터 가져오기 성공:", response.data);
            scoreChangeData = response.data;
            scoreChangeChart(scoreChangeData, selectedDifficulty, studentId);
        })
        .catch(error => {
            console.error("데이터 가져오기 실패:", error);
        });
}

const ctx5 = document.getElementById('scoreTrendChart').getContext('2d');
let scoreTrendChart = null;

function scoreChangeChart(data, selectedDifficulty, studentId) {
    const filteredData = data.filter(item => item.difficulty === selectedDifficulty && item.userId === parseInt(studentId));

    const studentName = filteredData.length > 0 ? filteredData[0].name : "";

    const maxRound = Math.max(...filteredData.map(item => item.round), 0);
    const labels = Array.from({ length: maxRound }, (_, i) => `${i + 1}회차`);
    const scores = Array(maxRound).fill(null);
    filteredData.forEach(item => {
        scores[item.round - 1] = item.score;
    });

    const dataset = [{
        label: studentName,
        data: scores,
        borderColor: getRandomColor(),
        fill: false
    }];

    if (scoreTrendChart) {
        scoreTrendChart.data.labels = labels;
        scoreTrendChart.data.datasets = dataset;
        scoreTrendChart.update();
    } else {
        scoreTrendChart = new Chart(ctx5, {
            type: 'line',
            data: {
                labels: labels,
                datasets: dataset
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        max: 100
                    }
                },
                plugins: {
                    tooltip: {
                        callbacks: {
                            label: (context) => `점수: ${context.raw}`
                        }
                    }
                }
            }
        });
    }
}


// 랜덤 색상 생성 함수
function getRandomColor() {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}