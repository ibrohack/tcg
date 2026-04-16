package com.dami.tcg.modelo;

/**
 * Model class representing a trading card in the TCG application.
 * <p>
 * Each card has a unique identifier, a name, a quality/rarity tier (e.g., Common, Rare,
 * Epic, Legendary, Mythic), a description, and buy/sell prices used in the shop system.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 */
public class Card {
	/** The unique identifier of the card. */
	private int cardID;

	/** The display name of the card. */
	private String name;

	/** The quality/rarity tier of the card (e.g., Common, Rare, Epic, Legendary, Mythic). */
	private String quality;

	/** A textual description of the card. */
	private String description;

	/** The price to purchase this card from the shop (in coins). */
	private int purchasePrice;

	/** The price to sell this card (in coins). */
	private int sellPrice;

	/**
	 * Default constructor that initializes the card with empty/default values.
	 */
	public Card() {
		this.cardID = 0;
		this.name = "";
		this.quality = "";
		this.description = "";
		this.purchasePrice = 0;
		this.sellPrice = 0;
	}

	/**
	 * Parameterized constructor to create a card with all fields specified.
	 *
	 * @param cardID        the unique identifier of the card
	 * @param name          the display name of the card
	 * @param quality       the quality/rarity tier of the card
	 * @param description   a textual description of the card
	 * @param purchasePrice the purchase price in coins
	 * @param sellPrice     the sell price in coins
	 */
	public Card(int cardID, String name, String quality, String description, int purchasePrice, int sellPrice) {
		this.cardID = cardID;
		this.name = name;
		this.quality = quality;
		this.description = description;
		this.purchasePrice = purchasePrice;
		this.sellPrice = sellPrice;
	}

	/**
	 * Returns the unique identifier of this card.
	 *
	 * @return the card ID
	 */
	public int getCardID() {
		return cardID;
	}

	/**
	 * Sets the unique identifier of this card.
	 *
	 * @param cardID the card ID to set
	 */
	public void setCardID(int cardID) {
		this.cardID = cardID;
	}

	/**
	 * Returns the display name of this card.
	 *
	 * @return the card name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the display name of this card.
	 *
	 * @param name the card name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the quality/rarity tier of this card.
	 *
	 * @return the quality string (e.g., "Common", "Rare", "Epic", "Legendary", "Mythic")
	 */
	public String getQuality() {
		return quality;
	}

	/**
	 * Sets the quality/rarity tier of this card.
	 *
	 * @param quality the quality string to set
	 */
	public void setQuality(String quality) {
		this.quality = quality;
	}

	/**
	 * Returns the textual description of this card.
	 *
	 * @return the card description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the textual description of this card.
	 *
	 * @param description the card description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the purchase price of this card in coins.
	 *
	 * @return the purchase price
	 */
	public int getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * Sets the purchase price of this card in coins.
	 *
	 * @param purchasePrice the purchase price to set
	 */
	public void setPurchasePrice(int purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	/**
	 * Returns the sell price of this card in coins.
	 *
	 * @return the sell price
	 */
	public int getSellPrice() {
		return sellPrice;
	}

	/**
	 * Sets the sell price of this card in coins.
	 *
	 * @param sellPrice the sell price to set
	 */
	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	/**
	 * Returns a string representation of this card, including its ID, name, quality,
	 * and description.
	 *
	 * @return a string representation of the card
	 */
	@Override
	public String toString() {
		return "Card [cardID=" + cardID + ", name=" + name + ", quality=" + quality + ", description=" + description
				+ "]";
	}
}