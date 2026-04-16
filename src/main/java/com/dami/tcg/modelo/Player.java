package com.dami.tcg.modelo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Model class representing a player in the TCG application.
 * <p>
 * Stores the player's credentials, coin balance, card collection (as a map of
 * card IDs to quantities), deck list, and an optional avatar URL for profile pictures.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 */
public class Player {
	/** The unique identifier of the player. */
	private int playerId;

	/** The player's login username. */
	private String username;

	/** The player's hashed password (BCrypt). */
	private String password;

	/** The player's current coin balance. */
	private int coins;

	/** A map of card IDs to their quantities in the player's collection. */
	private HashMap<Integer, Integer> cards;

	/** The list of decks owned by the player. */
	private ArrayList<Deck> decks;

	/** Transient field to store the player's profile picture URL. */
	private String avatarUrl;

	/**
	 * Default constructor.
	 */
	public Player() {
	}

	/**
	 * Parameterized constructor to create a player with all fields specified.
	 *
	 * @param playerId the unique identifier of the player
	 * @param username the player's username
	 * @param password the player's hashed password
	 * @param coins    the player's coin balance
	 * @param cards    a map of card IDs to their quantities
	 * @param decks    the list of decks owned by the player
	 */
	public Player(int playerId, String username, String password, int coins, HashMap<Integer, Integer> cards,
			ArrayList<Deck> decks) {
		this.playerId = playerId;
		this.username = username;
		this.password = password;
		this.coins = coins;
		this.cards = cards;
		this.decks = decks;
	}

	/**
	 * Returns the player's avatar URL.
	 *
	 * @return the avatar URL, or {@code null} if no avatar is set
	 */
	public final String getAvatarUrl() {
		return avatarUrl;
	}

	/**
	 * Sets the player's avatar URL.
	 *
	 * @param avatarUrl the avatar URL to set
	 */
	public final void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	/**
	 * Returns the player's card collection as a map of card IDs to quantities.
	 *
	 * @return a {@link HashMap} mapping card IDs to quantities
	 */
	public final HashMap<Integer, Integer> getCards() {
		return cards;
	}

	/**
	 * Sets the player's card collection.
	 *
	 * @param cards a {@link HashMap} mapping card IDs to quantities
	 */
	public final void setCards(HashMap<Integer, Integer> cards) {
		this.cards = cards;
	}

	/**
	 * Returns the list of decks owned by the player.
	 *
	 * @return an {@link ArrayList} of {@link Deck} objects
	 */
	public final ArrayList<Deck> getDecks() {
		return decks;
	}

	/**
	 * Sets the list of decks owned by the player.
	 *
	 * @param decks an {@link ArrayList} of {@link Deck} objects
	 */
	public final void setDecks(ArrayList<Deck> decks) {
		this.decks = decks;
	}

	/**
	 * Returns the unique identifier of this player.
	 *
	 * @return the player ID
	 */
	public final int getPlayerId() {
		return playerId;
	}

	/**
	 * Sets the unique identifier of this player.
	 *
	 * @param playerId the player ID to set
	 */
	public final void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	/**
	 * Returns the player's username.
	 *
	 * @return the username
	 */
	public final String getUsername() {
		return username;
	}

	/**
	 * Sets the player's username.
	 *
	 * @param username the username to set
	 */
	public final void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Returns the player's hashed password.
	 *
	 * @return the hashed password
	 */
	public final String getPassword() {
		return password;
	}

	/**
	 * Sets the player's hashed password.
	 *
	 * @param password the hashed password to set
	 */
	public final void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns the player's current coin balance.
	 *
	 * @return the coin balance
	 */
	public final int getCoins() {
		return coins;
	}

	/**
	 * Sets the player's coin balance.
	 *
	 * @param coins the coin balance to set
	 */
	public final void setCoins(int coins) {
		this.coins = coins;
	}

	/**
	 * Returns a string representation of this player, including ID, username,
	 * password, cards, and decks.
	 *
	 * @return a string representation of the player
	 */
	@Override
	public String toString() {
		return "Player [playerId=" + playerId + ", username=" + username + ", password=" + password + ", cards=" + cards
				+ ", decks=" + decks + "]";
	}

}
