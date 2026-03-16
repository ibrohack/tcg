package com.dami.tcg.controller;

import java.util.List;

import com.dami.tcg.modelo.*;

public class DeckController {
	DeckDAO dao = new ImplDeckBD();
	
	
	
	public boolean checkCard(Deck deck) {
		return dao.checkCard(deck);
	}

	public boolean insertDeck(Deck deck) {
		return dao.insertDeck(deck);
	}

	public boolean deleteDeck(Deck deck) {
		return dao.deleteDeck(deck);
	}

	public boolean updateDeck(Deck deck) {
		return dao.updateDeck(deck);
	}

	public List<Deck> queryAll(){
		return dao.queryAll();
	}

	public Deck queryDeck(int deckId) {
		return dao.queryDeck(deckId);
	}
	
}