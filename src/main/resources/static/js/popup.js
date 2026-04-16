// Change the form action to the URL of the button choosen by the user, 
// and open the popup window if the user clicks on a button with the class "popupOpen"

// Get the modal, the button that opens it, and the button that closes it
var modal = document.getElementById("popup");
var popupOpenButtons = document.querySelectorAll(".popupOpen");
var popupClose = document.getElementById("popupClose");

popupOpenButtons.forEach(function (button) {
    button.onclick = function () {
        modal.classList.remove("hidden");
        const modifyForm = modal.querySelector("#modify-form");
        if (modifyForm) {
            if (modifyForm.tagName == "FORM") {
                modifyForm.setAttribute("action", this.getAttribute("data-action"));
            } else if (modifyForm.tagName == "A") {
                modifyForm.setAttribute("href", this.getAttribute("data-action"));
            }
        }
    }
});

popupClose.onclick = function () {
    modal.classList.add("hidden");
}

// Close the modal if the user clicks anywhere outside of it
window.onclick = function (event) {
    if (event.target == modal) {
        modal.classList.add("hidden");
    }
}