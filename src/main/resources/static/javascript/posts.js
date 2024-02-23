document.getElementById('searchInput').addEventListener('input', function () {

    let searchInput = document.getElementById('searchInput');
    let searchValue = searchInput.value.trim();

    if (searchValue.length >= 0) {
        fetch('/posts/search?title=' + searchValue, {
            method: 'GET'
        }).then(response => response.json())
            .then(data => {

                if (data.length === 0) {
                    document.querySelector(".row").textContent = '';
                    let div = document.createElement('div');
                    div.className = 'well blog';
                    div.innerHTML = `
                            <h2 style="color: #2d2525;text-align: center">Aradığınız başlık bulunamadı.</h2>
                        `;
                    document.querySelector(".row").appendChild(div);
                } else {
                    showPosts(data);
                }
            })
    }
});

function showPosts(data) {
    document.querySelector(".row").textContent = '';
    document.querySelector("#pagination-shows").textContent = '';

    const options = {
        weekday: 'short',
        day: '2-digit',
        month: 'long',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        timeZone: 'Europe/Istanbul'
    };

    data.forEach(post => {
        let wellBlogDiv = document.createElement('div');
        let col = document.createElement('div');

        let createdDate = new Date(post.createdDate);
        let formattedDate = new Intl.DateTimeFormat('tr-TR', options).format(createdDate);

        let parts = formattedDate.split(' ');
        let day = parts[0];
        let monthName = parts[1];
        let year = parts[2];
        let dayName = parts[3];
        let time = parts[4];

        // Cum, 09 Şubat 2024 17:05:21
        formattedDate = `${dayName}, ${day} ${monthName} ${year} ${time}`;

        col.className = 'col-xs-12 col-sm-12 col-md-12 col-lg-12';

        wellBlogDiv.className = 'well blog';
        wellBlogDiv.innerHTML = `
                <div class="date primary" style="width: fit-content; height: fit-content; padding: 1px; border-radius: 5px; background-color: #007bff; color: #ffffff;">
                   ${formattedDate}
                </div>
                <div class="row">
                    <div class="col-xs-12 col-sm-8 col-md-8 col-lg-8">
                        <div class="image">
                            <img src="https://blog.aku.edu.tr/wp-content/themes/akulog/images/giris.jpg" alt="">
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-4 col-md-4 col-lg-4">
                        <div class="blog-details">
                            <a style="text-decoration: none" href="/posts/${post.id}">
                                <h2>${post.title}</h2>
                            </a>
                            <p>${post.shortInformation}</p>
                        </div>
                    </div>
                </div>
            `;

        col.appendChild(wellBlogDiv);
        document.querySelector(".row").appendChild(col);
    });
}