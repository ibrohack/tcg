package com.dami.tcg.modelo;

import java.util.ArrayList;

public class Deck {
	private int deckID;
	private String title;
	private String description;
	private ArrayList<Card> aCard;
	
	public Deck() {
		this.deckID = 0;
		this.title = "";
		this.description = "";
		this.aCard = new ArrayList<Card>();
	}

	public Deck(int deckID, String title, String description, ArrayList<Card> aCard) {
		this.deckID = deckID;
		this.title = title;
		this.description = description;
		this.aCard = aCard;
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
	public ArrayList<Card> getaCard() {
		return aCard;
	}
	public void setaCard(ArrayList<Card> aCard) {
		this.aCard = aCard;
	}
	
	@Override
	public String toString() {
		return "Deck [deckID=" + deckID + ", title=" + title + ", description=" + description + ", aCard=" + aCard+ "]";
	}
}