package com.dami.tcg.modelo;

import java.util.HashMap;

public interface PlayerDAO {
    public boolean checkPlayer(Player player);

    public boolean insertPlayer(Player player);

    public boolean deletePlayer(Player player);

    public boolean updatePlayer(Player player);

    public Player queryPlayer(int playerID);

    public HashMap<Integer, Integer> queryPlayerCards(int playerID);

    public Player queryPlayerByUsername(String username);

    public boolean addCard(Player player, Card card);

    public int getGold(Player player);

    public void buyPack(Player player);

    public void addCoins(Player player, int gold);

    public boolean checkFreePack(Player player);

    public boolean freePackOpend(Player player);
}