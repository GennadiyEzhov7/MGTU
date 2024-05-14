// document.getElementById("downloadButton").addEventListener("click", function() {
//     var container = document.querySelector(".result-container");
//     html2canvas(container, {
//         logging: true, // Включаем логирование, чтобы увидеть сообщения об ошибках, если они возникнут
//         useCORS: true // Устанавливаем флаг useCORS в true для обработки изображений с других доменов
//     }, function(canvas) {
//         var link = document.createElement("a");
//         document.body.appendChild(link);
//         link.download = "test.png";
//         link.href = canvas.toDataURL();
//         link.click();
//         document.body.removeChild(link);
//     });
// });

document.getElementById("downloadButton").addEventListener("click", function() {
    html2canvas(document.querySelector(".result-container")).then(canvas =>{
        var link = document.createElement("a");
        document.body.appendChild(link);
        link.download = "filename.jpg";
        link.href = canvas.toDataURL();
        link.target = '_blank';
        link.click();
    });
});