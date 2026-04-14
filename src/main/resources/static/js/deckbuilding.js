function addCardToDeck(cardId) {
    if (parseInt(document.getElementById("deck-header").querySelector("#deck-quantity").innerText.split("/")[0]) >= 30) {
        console.log("Deck is full");
        return;
    }
    else {
        document.getElementById("deck-header").querySelector("#deck-quantity").innerText = parseInt(document.getElementById("deck-header").querySelector("#deck-quantity").innerText.split("/")[0]) + 1 + "/30";
    }
    if ($("div#" + cardId).length > 0) {
        let cardDiv = document.getElementById(cardId);
        let quantity = cardDiv.querySelector(".card-quantity-display");
        quantity.innerText = parseInt(quantity.innerText) + 1;
        document.getElementById("card-" + cardId).querySelector(".card-quantity-display").innerText = parseInt(quantity.innerText) + 1;
    } else {
        let template = document.getElementById("card-template");
        let clone = template.content.cloneNode(true);
        let cardDiv = clone.querySelector(".item-card-div");

        cardDiv.id = cardId;
        document.getElementById("card-" + cardId).querySelector(".card-quantity-display").innerText = parseInt(document.getElementById("card-" + cardId).querySelector(".card-quantity-display").innerText) + 1;
        clone.querySelector(".card-id-display").innerText = cardId;
        clone.querySelector(".hidden-card-id").value = cardId;
        clone.querySelector(".card-quantity-display").innerText = 1;
        clone.querySelector(".card-name-display").innerText = document.getElementById("card-" + cardId).querySelector(".card-name-display").innerText;

        let removeBtn = clone.querySelector(".remove-btn");
        removeBtn.onclick = function () {
            removeCard(cardId);
        };

        $("#deck-sidebar .divide-y").append(clone);
        console.log("Added cardId: " + cardId);
    }
}

function removeCard(cardId) {
    let cardDiv = document.getElementById(cardId);
    if (document.getElementById("deck-header").querySelector("#deck-quantity").innerText.split("/")[0] <= 0) {
        console.log("Deck doesnt have cards");
        return;
    }
    else {
        document.getElementById("deck-header").querySelector("#deck-quantity").innerText = parseInt(document.getElementById("deck-header").querySelector("#deck-quantity").innerText.split("/")[0]) - 1 + "/30";
    }
    if (!cardDiv) {
        return;
    }

    let quantityDisplay = cardDiv.querySelector(".card-quantity-display");
    let currentQty = quantityDisplay.innerText;

    if (currentQty <= 1) {
        cardDiv.remove();
        document.getElementById("card-" + cardId).querySelector(".card-quantity-display").innerText = parseInt(currentQty) - 1;
    } else {
        quantityDisplay.innerText = currentQty - 1;
        document.getElementById("card-" + cardId).querySelector(".card-quantity-display").innerText = parseInt(currentQty) - 1;
    }
    console.log("Removed cardId: " + cardId);
}
function addCardsToHiddenContainer() {
    const container = document.getElementById("hidden-cards-container");
    if (container) {
        container.innerHTML = "";
        const cardDivs = document.querySelectorAll("#deck-sidebar .item-card-div");
        cardDivs.forEach(cardDiv => {
            const cardId = cardDiv.querySelector(".hidden-card-id").value;
            const quantity = cardDiv.querySelector(".card-quantity-display").innerText;

            const input = document.createElement("input");
            input.type = "hidden";
            input.name = "cards[" + cardId + "]";
            input.value = quantity;
            container.appendChild(input);
        });
    }
    console.log("Added cards to hidden container");
}
document.addEventListener("DOMContentLoaded", function () {
    const deckForm = document.getElementById("deck-form");
    if (deckForm) {
        deckForm.addEventListener("submit", function (e) {
            addCardsToHiddenContainer();
        });
    }
});