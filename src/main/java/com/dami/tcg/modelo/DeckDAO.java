package com.dami.tcg.modelo;

import java.util.List;

public interface DeckDAO {
	public boolean checkCard(Deck deck);

	public boolean insertDeck(Deck deck);

	public boolean deleteDeck(Deck deck);

	public boolean updateDeck(Deck deck);

	public List<Deck> queryAll();

	public Deck queryDeck(int deckId);

	public List<Card> queryPlayerCards(int playerId);

	public List<Deck> queryPlayerDecks(int playerId);
}