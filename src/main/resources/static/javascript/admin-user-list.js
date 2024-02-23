function lockUser(userId) {
    fetch(`/admin/lock-user?userId=${userId}`, {
        method: 'GET'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Locking user failed');
            }

            document.getElementById(`lockUser_${userId}`).className = 'fas fa-lock-open text-success';
            document.getElementById(`unlockedUser_${userId}`).textContent = 'Kilitli';
            document.getElementById(`unlockedUser_${userId}`).style.color = '#ff2020';
        })
        .catch(error => {
            console.error('Locking user error:', error);
        });
}

function unlockUser(userId) {
    fetch(`/admin/unlock-user?userId=${userId}`, {
        method: 'GET'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Unlocking user failed');
            }

            document.getElementById(`unlockUser_${userId}`).className = 'fas fa-lock text-danger';
            document.getElementById(`lockedUser_${userId}`).textContent = 'Mevcut';
            document.getElementById(`lockedUser_${userId}`).style.color = '#00d504';
        })
        .catch(error => {
            console.error('Unlocking user error:', error);
        });
}