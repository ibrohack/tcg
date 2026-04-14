package com.dami.tcg.modelo;

import java.util.HashMap;

public class Deck {
	private int deckID;
	private String title;
	private String description;
	private int playerID;
	private HashMap<Integer, Integer> cards;

	public Deck() {
		this.deckID = 0;
		this.title = "";
		this.description = "";
		this.playerID = 0;
		this.cards = new HashMap<Integer, Integer>();
	}

	public Deck(int deckID, String title, String description, int playerID, HashMap<Integer, Integer> cards) {
		this.deckID = deckID;
		this.title = title;
		this.description = description;
		this.playerID = playerID;
		this.cards = cards;
	}

	public Deck(int deckID, String title, String description, HashMap<Integer, Integer> cards) {
		this.deckID = deckID;
		this.title = title;
		this.description = description;
		this.playerID = 0;
		this.cards = cards;
	}

	public int getDeckID() {
		return deckID;
	}

	public void setDeckID(int deckID) {
		this.deckID = deckID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	public HashMap<Integer, Integer> getCards() {
		return cards;
	}

	public void setCards(HashMap<Integer, Integer> cards) {
		this.cards = cards;
	}

	public int getCardCount() {
		int count = 0;
		for (Integer q : cards.values()) {
			count += q;
		}
		return count;
	}

	@Override
	public String toString() {
		return "Deck [deckID=" + deckID + ", title=" + title + ", description=" + description + ", cards=" + cards
				+ "]";
	}
}