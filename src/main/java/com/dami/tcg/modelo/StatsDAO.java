package com.dami.tcg.modelo;

import java.util.List;

/**
 * Data Access Object interface for retrieving game statistics.
 * <p>
 * Defines the contract for querying aggregate data used on the home page dashboard,
 * including player counts, card popularity metrics, unclaimed cards, and latest releases.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 * @see ImplStatsBD
 */
public interface StatsDAO {

    /**
     * Returns the total number of registered players.
     *
     * @return the active player count
     */
    public int getActivePlayersCount();

    /**
     * Retrieves the card that appears most frequently across all player collections.
     *
     * @return the most common {@link Card}, or {@code null} if no cards exist
     */
    public Card getMostCommonCard();

    /**
     * Returns the total quantity of the most commonly owned card across all players.
     *
     * @return the total quantity of the most common card
     */
    public int getMostCommonCardQuantity();

    /**
     * Retrieves the card that appears least frequently across all player collections.
     *
     * @return the least found {@link Card}, or {@code null} if no cards exist
     */
    public Card getLeastFoundCard();

    /**
     * Returns the total quantity of the least commonly owned card across all players.
     *
     * @return the total quantity of the least found card
     */
    public int getLeastFoundCardQuantity();

    /**
     * Retrieves all cards that are not owned by any player.
     *
     * @return a {@link List} of unclaimed {@link Card} objects
     */
    public List<Card> getUnclaimedCards();

    /**
     * Retrieves the most recently added cards, ordered by card ID descending.
     *
     * @param limit the maximum number of cards to retrieve
     * @return a {@link List} of the latest {@link Card} objects
     */
    public List<Card> getLatestCards(int limit);
}
