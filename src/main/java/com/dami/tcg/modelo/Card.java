package com.dami.tcg.modelo;

public class Card {
	private int cardID;
	private String name;
	private String quality;
	private String description;
	private int purchasePrice;
	private int sellPrice;

	public Card() {
		this.cardID = 0;
		this.name = "";
		this.quality = "";
		this.description = "";
		this.purchasePrice = 0;
		this.sellPrice = 0;
	}

	public Card(int cardID, String name, String quality, String description, int purchasePrice, int sellPrice) {
		this.cardID = cardID;
		this.name = name;
		this.quality = quality;
		this.description = description;
		this.purchasePrice = purchasePrice;
		this.sellPrice = sellPrice;
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

	public int getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(int purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public int getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	@Override
	public String toString() {
		return "Card [cardID=" + cardID + ", name=" + name + ", quality=" + quality + ", description=" + description
				+ "]";
	}
}