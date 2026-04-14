function addCardToDeck(cardId) {
    if (parseInt(document.getElementById("deck-header").querySelector("#deck-quantity").innerText.split("/")[0]) >= 30) {
    }
    else {
        if ($("div#" + cardId).length > 0) {
            let cardDiv = document.getElementById(cardId);
            let quantity = cardDiv.querySelector(".card-quantity-display");
            if (parseInt(document.getElementById("card-" + cardId).querySelector(".card-quantity-display").innerText) > 0) {
                document.getElementById("deck-header").querySelector("#deck-quantity").innerText = parseInt(document.getElementById("deck-header").querySelector("#deck-quantity").innerText.split("/")[0]) + 1 + "/30";
                quantity.innerText = parseInt(quantity.innerText) - 1;
                document.getElementById("card-" + cardId).querySelector(".card-quantity-display").innerText = parseInt(quantity.innerText) - 1;
            }
        } else {
            let template = document.getElementById("card-template");
            let clone = template.content.cloneNode(true);
            let cardDiv = clone.querySelector(".item-card-div");
            cardDiv.id = cardId;
            document.getElementById("deck-header").querySelector("#deck-quantity").innerText = parseInt(document.getElementById("deck-header").querySelector("#deck-quantity").innerText.split("/")[0]) + 1 + "/30";
            document.getElementById("card-" + cardId).querySelector(".card-quantity-display").innerText = parseInt(document.getElementById("card-" + cardId).querySelector(".card-quantity-display").innerText) - 1;
            clone.querySelector(".card-id-display").innerText = cardId;
            clone.querySelector(".hidden-card-id").value = cardId;
            clone.querySelector(".card-quantity-display").innerText = 1;
            clone.querySelector(".card-name-display").innerText = document.getElementById("card-" + cardId).querySelector(".card-name-display").innerText;

            let removeBtn = clone.querySelector(".remove-btn");
            removeBtn.onclick = function () { removeCard(cardId); };
            $("#deck-sidebar .divide-y").append(clone);
        }
    }
}

function removeCard(cardId) {
    let cardDiv = document.getElementById(cardId);
    document.getElementById("deck-header").querySelector("#deck-quantity").innerText = parseInt(document.getElementById("deck-header").querySelector("#deck-quantity").innerText.split("/")[0]) - 1 + "/30";

    let quantityDisplay = cardDiv.querySelector(".card-quantity-display");

    if (parseInt(quantityDisplay.innerText) <= 1) {
        cardDiv.remove();
        document.getElementById("card-" + cardId).querySelector(".card-quantity-display").innerText = parseInt(document.getElementById("card-" + cardId).querySelector(".card-quantity-display").innerText) + 1;
    } else {
        quantityDisplay.innerText = parseInt(quantityDisplay.innerText) - 1;
        document.getElementById("card-" + cardId).querySelector(".card-quantity-display").innerText = parseInt(document.getElementById("card-" + cardId).querySelector(".card-quantity-display").innerText) + 1;
    }
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
}
document.addEventListener("DOMContentLoaded", function () {
    const deckForm = document.getElementById("deck-form");
    if (deckForm) {
        deckForm.addEventListener("submit", function (e) {
            addCardsToHiddenContainer();
        });
    }
});