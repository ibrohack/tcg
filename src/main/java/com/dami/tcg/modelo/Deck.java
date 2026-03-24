package com.dami.tcg.modelo;

import java.util.HashMap;

public class Deck {
	private int deckID;
	private String title;
	private String description;
	private HashMap<Integer, Integer> cards;

	public Deck() {
		this.deckID = 0;
		this.title = "";
		this.description = "";
		this.cards = new HashMap<Integer, Integer>();
	}

	public Deck(int deckID, String title, String description, HashMap<Integer, Integer> cards) {
		this.deckID = deckID;
		this.title = title;
		this.description = description;
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

	public HashMap<Integer, Integer> getCards() {
		return cards;
	}

	public void setCards(HashMap<Integer, Integer> cards) {
		this.cards = cards;
	}

	@Override
	public String toString() {
		return "Deck [deckID=" + deckID + ", title=" + title + ", description=" + description + ", cards=" + cards
				+ "]";
	}
}