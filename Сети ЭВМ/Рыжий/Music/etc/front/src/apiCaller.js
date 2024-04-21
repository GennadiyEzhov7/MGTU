export default function callApi(router, url, data, callback) {
    let status;
    fetch(url, data)
        .then(response => {
            status = response.status;
            if (response.status == 401) {
                router.push('/authorize')
                return;
            } else {
                return response.json();
            }
        }).then(data => {
            callback(status, data);
        });
}