var modal = document.getElementById("popup");
var popupOpen = document.getElementById("popupOpen");
var popupClose = document.getElementById("popupClose");

popupOpen.onclick = function () {
    modal.classList.remove("hidden");
}

popupClose.onclick = function () {
    modal.classList.add("hidden");
}

window.onclick = function (event) {
    if (event.target == modal) {
        modal.classList.add("hidden");
    }
}