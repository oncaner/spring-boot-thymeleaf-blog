let page = 0;
let userId = parseInt(document.getElementById("postUserId").value);
console.log(userId);

document.getElementById("buttonRest").addEventListener("click", function () {
    page++;

    fetch(`/posts/pageable?page=${page}&userId=${userId}`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            showPostsData(data);
        })
        .catch(error => {
            console.error('Fetch hatası:', error);
        });
});

function showPostsData(data) {
    data.forEach(post => {
        const tr = document.createElement('tr');
        tr.id = 'post-process-data';
        if (post.userDTO.email === post.principalEmail) {
            tr.innerHTML = `
                <td>${post.id}</td>
                <td>${post.title}</td>
                <td>
                    <a href="/posts/update-post/${post.id}" target="_blank" class="btn btn-primary">Güncelle</a>
                    <a href="/posts/${post.id}" target="_blank" class="btn btn-primary">Göster</a>
                </td>
            `;
        } else {
            tr.innerHTML = `
                <td>${post.id}</td>
                <td>${post.title}</td>
                <td>
                    <a href="/posts/${post.id}" target="_blank" class="btn btn-primary">Göster</a>
                </td>
            `;
        }

        document.getElementById('postDatas').appendChild(tr);
    });
}
