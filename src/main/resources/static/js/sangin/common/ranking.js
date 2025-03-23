document.addEventListener('DOMContentLoaded', function() {
    // 랭킹 데이터 가져오기
    fetchRankingData();

    // 새로고침 버튼 이벤트 추가
    document.getElementById('refresh-btn').addEventListener('click', function() {
        fetchRankingData();
    });
});

// Axios를 사용하여 랭킹 데이터 가져오기
function fetchRankingData() {
    // 로딩 상태 표시
    showLoadingState();

    // Axios를 사용하여 서버에서 데이터 가져오기
    axios.get('/api/common/rankings')
        .then(function(response) {
            // 서버로부터 받은 데이터
            const data = response.data;

            // 데이터가 있는지 확인
            if (data && data.rankings && data.rankings.length > 0) {
                // 데이터 업데이트
                updateRankingData(data);
            } else {
                showErrorMessage('데이터가 없습니다.');
            }
        })
        .catch(function(error) {
            console.error('랭킹 데이터를 가져오는 중 오류 발생:', error);
            showErrorMessage('데이터를 가져오는 중 오류가 발생했습니다.');
        })
        .finally(function() {
            // 로딩 상태 해제
            hideLoadingState();

            // 마지막 업데이트 시간 표시
            document.getElementById('last-updated').textContent = formatDate(new Date());
        });
}

// 랭킹 데이터 업데이트
function updateRankingData(data) {
    // 총 참여자 수 업데이트
    document.getElementById('total-participants').textContent = data.totalParticipants || 0;

    const rankings = data.rankings;

    // 상위 3명 데이터 설정
    if (rankings.length > 0) {
        // 1등
        document.getElementById('first-name').textContent = rankings[0].name || '이름 없음';
        document.getElementById('first-score').textContent = `${rankings[0].score || 0}점`;
        document.getElementById('first-detail').textContent =
            `정답률: ${rankings[0].correctRate || 0}% | 시도: ${rankings[0].attempts || 0}회`;

        // 2등(있을 경우)
        if (rankings.length > 1) {
            document.getElementById('second-name').textContent = rankings[1].name || '이름 없음';
            document.getElementById('second-score').textContent = `${rankings[1].score || 0}점`;
            document.getElementById('second-detail').textContent =
                `정답률: ${rankings[1].correctRate || 0}% | 시도: ${rankings[1].attempts || 0}회`;
        } else {
            resetPodium('second');
        }

        // 3등(있을 경우)
        if (rankings.length > 2) {
            document.getElementById('third-name').textContent = rankings[2].name || '이름 없음';
            document.getElementById('third-score').textContent = `${rankings[2].score || 0}점`;
            document.getElementById('third-detail').textContent =
                `정답률: ${rankings[2].correctRate || 0}% | 시도: ${rankings[2].attempts || 0}회`;
        } else {
            resetPodium('third');
        }
    } else {
        // 데이터가 없는 경우 모든 시상대 초기화
        resetPodium('first');
        resetPodium('second');
        resetPodium('third');
    }

    // 4위 이하 랭킹 업데이트
    updateOtherRankings(rankings.slice(3));
}

// 시상대 정보 초기화
function resetPodium(position) {
    document.getElementById(`${position}-name`).textContent = '-';
    document.getElementById(`${position}-score`).textContent = '-';
    document.getElementById(`${position}-detail`).textContent = '-';
}

// 4위 이하 랭킹 업데이트
function updateOtherRankings(otherRankings) {
    const othersList = document.getElementById('others-list');
    othersList.innerHTML = ''; // 기존 내용 초기화

    if (otherRankings.length === 0) {
        // 4위 이하 데이터가 없을 경우
        const noDataElement = document.createElement('div');
        noDataElement.className = 'other-item';
        noDataElement.innerHTML = '<span colspan="5" style="text-align: center; grid-column: span 5;">4위 이하 랭킹 데이터가 없습니다.</span>';
        othersList.appendChild(noDataElement);
        return;
    }

    // 4위부터 순위 표시
    otherRankings.forEach((ranking, index) => {
        const rank = index + 4; // 4위부터 시작

        const otherItem = document.createElement('div');
        otherItem.className = 'other-item';

        // 난이도에 따른 클래스 추가
        if (ranking.difficulty === 'HARD') {
            otherItem.classList.add('hard-difficulty');
        } else if (ranking.difficulty === 'MEDIUM') {
            otherItem.classList.add('medium-difficulty');
        }

        otherItem.innerHTML = `
            <span>${rank}</span>
            <span>${ranking.name || '이름 없음'}</span>
            <span>${ranking.score || 0}점</span>
            <span>${ranking.correctRate || 0}%</span>
            <span>${getDifficultyLabel(ranking.difficulty)}</span>
        `;

        othersList.appendChild(otherItem);
    });
}

// 난이도 레이블 변환
function getDifficultyLabel(difficulty) {
    switch (difficulty) {
        case 'EASY': return '쉬움';
        case 'MEDIUM': return '보통';
        case 'HARD': return '어려움';
        default: return '기본';
    }
}

// 로딩 상태 표시
function showLoadingState() {
    // 첫 번째 호출 또는 이미 존재하는 로딩 요소 선택
    let loadingElement = document.querySelector('.loading-overlay');

    // 로딩 요소가 없으면 새로 생성
    if (!loadingElement) {
        loadingElement = document.createElement('div');
        loadingElement.className = 'loading-overlay';
        loadingElement.innerHTML = `
            <div class="loading-spinner"></div>
            <div class="loading-text">랭킹 데이터를 불러오는 중...</div>
        `;

        // CSS 스타일 추가
        const style = document.createElement('style');
        style.textContent = `
            .loading-overlay {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(255, 255, 255, 0.8);
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                z-index: 9999;
            }
            
            .loading-spinner {
                border: 5px solid #f3f3f3;
                border-top: 5px solid var(--primary-color);
                border-radius: 50%;
                width: 50px;
                height: 50px;
                animation: spin 1s linear infinite;
            }
            
            .loading-text {
                margin-top: 15px;
                color: var(--primary-dark);
                font-weight: bold;
            }
            
            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }
        `;

        document.head.appendChild(style);
        document.body.appendChild(loadingElement);
    } else {
        // 이미 존재하면 표시만 변경
        loadingElement.style.display = 'flex';
    }
}

// 로딩 상태 해제
function hideLoadingState() {
    const loadingElement = document.querySelector('.loading-overlay');
    if (loadingElement) {
        loadingElement.style.display = 'none';
    }
}

// 오류 메시지 표시
function showErrorMessage(message) {
    alert(message);
}

// 날짜 포맷팅 함수
function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

// 임시 데이터 (API 개발 전 테스트용)
// 실제 사용시 삭제 또는 주석 처리
function getMockData() {
    return {
        totalParticipants: 24,
        rankings: [
            { name: '김민준', score: 98, correctRate: 92, attempts: 35, difficulty: 'HARD' },
            { name: '이서연', score: 85, correctRate: 78, attempts: 42, difficulty: 'MEDIUM' },
            { name: '박지호', score: 79, correctRate: 68, attempts: 37, difficulty: 'MEDIUM' },
            { name: '최은지', score: 73, correctRate: 65, attempts: 32, difficulty: 'EASY' },
            { name: '정현우', score: 71, correctRate: 63, attempts: 28, difficulty: 'EASY' },
            { name: '김서영', score: 68, correctRate: 60, attempts: 25, difficulty: 'EASY' },
            { name: '박태양', score: 65, correctRate: 58, attempts: 30, difficulty: 'EASY' }
        ]
    };
}