let cards = document.getElementById("cards");
let storagedCards = localStorage.getItem("cards");

if(!storagedCards){
	localStorage.setItem("cards", {cards});
}

//cards.innerHTML = storagedCards;