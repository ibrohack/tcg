function addCardToDeck(cardId) {
    if ($("div#" + cardId).length > 0) {
        let cardDiv = document.getElementById(cardId);
        let quantity = cardDiv.querySelector(".card-quantity-display");
        quantity.innerText = parseInt(quantity.innerText) + 1;
        console.log("CARD ALREADY IN DECK: " + cardId);
    } else {
        let template = document.getElementById("card-template");
        let clone = template.content.cloneNode(true);
        let cardDiv = clone.querySelector(".item-card-div");

        cardDiv.id = cardId;

        clone.querySelector(".card-id-display").innerText = cardId;
        clone.querySelector(".hidden-card-id").value = cardId;
        clone.querySelector(".card-quantity-display").innerText = 1;
        clone.querySelector(".card-name-display").innerText = cardName;
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
    if (!cardDiv) return;

    let quantityDisplay = cardDiv.querySelector(".card-quantity-display");
    let currentQty = quantityDisplay.innerText;

    if (currentQty <= 1) {
        cardDiv.remove();
    } else {
        quantityDisplay.innerText = currentQty - 1;
    }
    console.log("Removed cardId: " + cardId);
}