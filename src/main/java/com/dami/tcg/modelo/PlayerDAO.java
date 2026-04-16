package com.dami.tcg.modelo;

import java.util.HashMap;

/**
 * Data Access Object interface for {@link Player} entities.
 * <p>
 * Defines the contract for all player-related database operations, including
 * CRUD operations, authentication queries, card management, coin transactions,
 * and free pack availability checks.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 * @see ImplPlayerBD
 */
public interface PlayerDAO {

    /**
     * Checks whether a given player exists in the database.
     *
     * @param player the {@link Player} to verify
     * @return {@code true} if the player exists, {@code false} otherwise
     */
    public boolean checkPlayer(Player player);

    /**
     * Inserts a new player into the database.
     *
     * @param player the {@link Player} to insert
     * @return {@code true} if the insertion was successful, {@code false} otherwise
     */
    public boolean insertPlayer(Player player);

    /**
     * Deletes a player from the database.
     *
     * @param player the {@link Player} to delete
     * @return {@code true} if the deletion was successful, {@code false} otherwise
     */
    public boolean deletePlayer(Player player);

    /**
     * Updates an existing player's information in the database.
     *
     * @param player the {@link Player} with updated information
     * @return {@code true} if the update was successful, {@code false} otherwise
     */
    public boolean updatePlayer(Player player);

    /**
     * Retrieves a player by their unique identifier.
     *
     * @param playerID the ID of the player to retrieve
     * @return the matching {@link Player}, or {@code null} if not found
     */
    public Player queryPlayer(int playerID);

    /**
     * Retrieves a player's card collection as a map of card IDs to quantities.
     *
     * @param playerID the ID of the player
     * @return a {@link HashMap} mapping card IDs to quantities
     */
    public HashMap<Integer, Integer> queryPlayerCards(int playerID);

    /**
     * Retrieves a player by their username.
     *
     * @param username the username to search for
     * @return the matching {@link Player}, or {@code null} if not found
     */
    public Player queryPlayerByUsername(String username);

    /**
     * Adds a card to a player's collection. If the player already owns the card,
     * increments the quantity; otherwise, creates a new entry with quantity 1.
     *
     * @param player the {@link Player} to add the card to
     * @param card   the {@link Card} to add
     * @return {@code true} if the operation was successful, {@code false} otherwise
     */
    public boolean addCard(Player player, Card card);

    /**
     * Retrieves the current coin balance of a player from the database.
     *
     * @param player the {@link Player} whose balance to retrieve
     * @return the player's coin balance
     */
    public int getGold(Player player);

    /**
     * Deducts the pack cost (500 coins) from a player's balance.
     *
     * @param player the {@link Player} purchasing the pack
     */
    public void buyPack(Player player);

    /**
     * Adds (or subtracts) coins to/from a player's balance.
     *
     * @param player the {@link Player} whose balance to modify
     * @param gold   the amount of coins to add (negative values to subtract)
     */
    public void addCoins(Player player, int gold);

    /**
     * Checks whether a player has a free pack available to open.
     *
     * @param player the {@link Player} to check
     * @return {@code true} if a free pack is available, {@code false} otherwise
     */
    public boolean checkFreePack(Player player);

    /**
     * Marks a player's free pack as opened (consumed).
     *
     * @param player the {@link Player} who opened the free pack
     * @return {@code true} if the operation was successful, {@code false} otherwise
     */
    public boolean freePackOpend(Player player);
}