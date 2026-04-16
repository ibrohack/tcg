package com.dami.tcg.modelo;

import java.util.List;

/**
 * Data Access Object interface for {@link Deck} entities.
 * <p>
 * Defines the contract for all deck-related database operations, including
 * CRUD operations and queries for player-specific decks and cards.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 * @see ImplDeckBD
 */
public interface DeckDAO {

	/**
	 * Checks whether a given deck exists in the database.
	 *
	 * @param deck the {@link Deck} to verify
	 * @return {@code true} if the deck exists, {@code false} otherwise
	 */
	public boolean checkCard(Deck deck);

	/**
	 * Inserts a new deck into the database, including its card associations.
	 *
	 * @param deck the {@link Deck} to insert
	 * @return {@code true} if the insertion was successful, {@code false} otherwise
	 */
	public boolean insertDeck(Deck deck);

	/**
	 * Deletes a deck and its card associations from the database.
	 *
	 * @param deck the {@link Deck} to delete
	 * @return {@code true} if the deletion was successful, {@code false} otherwise
	 */
	public boolean deleteDeck(Deck deck);

	/**
	 * Updates an existing deck's information in the database.
	 *
	 * @param deck the {@link Deck} with updated information
	 * @return {@code true} if the update was successful, {@code false} otherwise
	 */
	public boolean updateDeck(Deck deck);

	/**
	 * Retrieves all decks from the database.
	 *
	 * @return a {@link List} of all {@link Deck} objects
	 */
	public List<Deck> queryAll();

	/**
	 * Retrieves a deck by its unique identifier.
	 *
	 * @param deckId the ID of the deck to retrieve
	 * @return the matching {@link Deck}, or {@code null} if not found
	 */
	public Deck queryDeck(int deckId);

	/**
	 * Retrieves all cards owned by a specific player.
	 *
	 * @param playerId the ID of the player
	 * @return a {@link List} of {@link Card} objects belonging to the player
	 */
	public List<Card> queryPlayerCards(int playerId);

	/**
	 * Retrieves all decks belonging to a specific player.
	 *
	 * @param playerId the ID of the player
	 * @return a {@link List} of {@link Deck} objects belonging to the player
	 */
	public List<Deck> queryPlayerDecks(int playerId);
}