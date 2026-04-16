package com.dami.tcg.modelo;

import java.util.HashMap;

/**
 * Model class representing a deck of cards in the TCG application.
 * <p>
 * A deck belongs to a player and contains a collection of cards with their respective
 * quantities, stored as a {@link HashMap} mapping card IDs to quantities.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 */
public class Deck {
	/** The unique identifier of the deck. */
	private int deckID;

	/** The title/name of the deck. */
	private String title;

	/** A textual description of the deck. */
	private String description;

	/** The ID of the player who owns this deck. */
	private int playerID;

	/** A map of card IDs to their quantities within this deck. */
	private HashMap<Integer, Integer> cards;

	/**
	 * Default constructor that initializes the deck with empty/default values.
	 */
	public Deck() {
		this.deckID = 0;
		this.title = "";
		this.description = "";
		this.playerID = 0;
		this.cards = new HashMap<Integer, Integer>();
	}

	/**
	 * Parameterized constructor to create a deck with all fields specified.
	 *
	 * @param deckID      the unique identifier of the deck
	 * @param title       the title/name of the deck
	 * @param description a textual description of the deck
	 * @param playerID    the ID of the player who owns this deck
	 * @param cards       a map of card IDs to their quantities
	 */
	public Deck(int deckID, String title, String description, int playerID, HashMap<Integer, Integer> cards) {
		this.deckID = deckID;
		this.title = title;
		this.description = description;
		this.playerID = playerID;
		this.cards = cards;
	}

	/**
	 * Parameterized constructor to create a deck without specifying a player ID.
	 * <p>
	 * The player ID defaults to 0.
	 * </p>
	 *
	 * @param deckID      the unique identifier of the deck
	 * @param title       the title/name of the deck
	 * @param description a textual description of the deck
	 * @param cards       a map of card IDs to their quantities
	 */
	public Deck(int deckID, String title, String description, HashMap<Integer, Integer> cards) {
		this.deckID = deckID;
		this.title = title;
		this.description = description;
		this.playerID = 0;
		this.cards = cards;
	}

	/**
	 * Returns the unique identifier of this deck.
	 *
	 * @return the deck ID
	 */
	public int getDeckID() {
		return deckID;
	}

	/**
	 * Sets the unique identifier of this deck.
	 *
	 * @param deckID the deck ID to set
	 */
	public void setDeckID(int deckID) {
		this.deckID = deckID;
	}

	/**
	 * Returns the title/name of this deck.
	 *
	 * @return the deck title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title/name of this deck.
	 *
	 * @param title the deck title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the textual description of this deck.
	 *
	 * @return the deck description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the textual description of this deck.
	 *
	 * @param description the deck description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the ID of the player who owns this deck.
	 *
	 * @return the player ID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * Sets the ID of the player who owns this deck.
	 *
	 * @param playerID the player ID to set
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * Returns the map of card IDs to their quantities in this deck.
	 *
	 * @return a {@link HashMap} mapping card IDs to quantities
	 */
	public HashMap<Integer, Integer> getCards() {
		return cards;
	}

	/**
	 * Sets the map of card IDs to their quantities in this deck.
	 *
	 * @param cards a {@link HashMap} mapping card IDs to quantities
	 */
	public void setCards(HashMap<Integer, Integer> cards) {
		this.cards = cards;
	}

	/**
	 * Calculates the total number of cards in this deck, summing all quantities.
	 *
	 * @return the total card count
	 */
	public int getCardCount() {
		int count = 0;
		for (Integer q : cards.values()) {
			count += q;
		}
		return count;
	}

	/**
	 * Returns a string representation of this deck, including its ID, title,
	 * description, and card contents.
	 *
	 * @return a string representation of the deck
	 */
	@Override
	public String toString() {
		return "Deck [deckID=" + deckID + ", title=" + title + ", description=" + description + ", cards=" + cards
				+ "]";
	}
}