package com.dami.tcg.modelo;

public interface PlayerDAO {
    public boolean checkPlayer(Player player);

    public boolean insertPlayer(Player player);

    public boolean deletePlayer(Player player);

    public boolean updatePlayer(Player player);
    
    public Player queryPlayer(int playerID);
}