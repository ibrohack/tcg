package com.dami.tcg.modelo;

public class Card {
	private int cardID;
	private String name;
	private String quality;
	private String description;
	
	public Card() {
		this.cardID = 0;
		this.name = "";
		this.quality = "";
		this.description = "";
	}

	public Card(int cardID, String name, String quality, String description) {
		this.cardID = cardID;
		this.name = name;
		this.quality = quality;
		this.description = description;
	}

	public int getCardID() {
		return cardID;
	}
	public void setCardID(int cardID) {
		this.cardID = cardID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Card [cardID=" + cardID + ", name=" + name + ", quality=" + quality + ", description=" + description + "]";
	}
}