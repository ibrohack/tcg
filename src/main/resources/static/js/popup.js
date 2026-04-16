var modal = document.getElementById("popup");
var popupOpen = document.getElementById("popupOpen");
var popupClose = document.getElementById("popupClose");

popupOpen.onclick = function () {
    modal.classList.remove("hidden");
    modal.querySelector("#modify-form").setAttribute("href", popupOpen.getAttribute("name"));
}

popupClose.onclick = function () {
    modal.classList.add("hidden");
}

window.onclick = function (event) {
    if (event.target == modal) {
        modal.classList.add("hidden");
    }
}