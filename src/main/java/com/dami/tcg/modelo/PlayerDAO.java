package com.dami.tcg.modelo;

import java.util.HashMap;

public interface PlayerDAO {
    public boolean checkPlayer(Player player);

    public boolean insertPlayer(Player player);

    public boolean deletePlayer(Player player);

    public boolean updatePlayer(Player player);
    
    public Player queryPlayer(int playerID);
    
    public HashMap<Integer,Integer> queryCardPlayer(int playerID);
}