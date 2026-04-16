package com.dami.tcg.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object interface for {@link Card} entities.
 * <p>
 * Defines the contract for all card-related database operations, including
 * CRUD operations, random card generation, and shop-related queries.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 * @see ImplCardBD
 */
public interface CardDAO {

    /**
     * Checks whether a given card exists in the database.
     *
     * @param card the {@link Card} to verify
     * @return {@code true} if the card exists, {@code false} otherwise
     */
    public boolean checkCard(Card card);

    /**
     * Inserts a new card into the database.
     *
     * @param card the {@link Card} to insert
     * @return {@code true} if the insertion was successful, {@code false} otherwise
     */
    public boolean insertCard(Card card);

    /**
     * Deletes a card from the database.
     *
     * @param card the {@link Card} to delete
     * @return {@code true} if the deletion was successful, {@code false} otherwise
     */
    public boolean deleteCard(Card card);

    /**
     * Updates an existing card in the database.
     *
     * @param card the {@link Card} with updated information
     * @return {@code true} if the update was successful, {@code false} otherwise
     */
    public boolean updateCard(Card card);

    /**
     * Retrieves all cards from the database.
     *
     * @return a {@link List} of all {@link Card} objects
     */
    public List<Card> queryAll();

    /**
     * Retrieves a card by its name.
     *
     * @param name the name of the card to search for
     * @return the matching {@link Card}, or {@code null} if not found
     */
    public Card queryCard(String name);

    /**
     * Retrieves a random card from the database based on rarity probabilities.
     *
     * @return a randomly selected {@link Card}
     */
    public Card queryRandomCard();

    /**
     * Retrieves a card by its unique identifier.
     *
     * @param cardId the ID of the card to retrieve
     * @return the matching {@link Card}, or {@code null} if not found
     */
    public Card queryCardId(int cardId);

    /**
     * Checks whether a specific card has been purchased by a player in the shop.
     *
     * @param card   the {@link Card} to check
     * @param player the {@link Player} to check against
     * @return {@code true} if the card has been purchased, {@code false} otherwise
     */
    public boolean queryPurchasedCard(Card card, Player player);

    /**
     * Marks a shop card as purchased by the specified player.
     *
     * @param card   the {@link Card} being purchased
     * @param player the {@link Player} making the purchase
     * @return {@code true} if the purchase was recorded successfully, {@code false} otherwise
     */
    public boolean purchaseShopCard(Card card, Player player);

    /**
     * Retrieves the list of cards available in the shop for a given player.
     * <p>
     * If {@code playerId} is 0, returns a default set of random cards.
     * </p>
     *
     * @param playerId the ID of the player (0 for default/unauthenticated)
     * @return an {@link ArrayList} of {@link Card} objects available in the shop
     */
    public ArrayList<Card> queryShopCards(int playerId);
}
