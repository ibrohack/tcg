package com.dami.tcg.modelo;

import java.util.List;

public interface StatsDAO {
    public int getActivePlayersCount();
    public Card getMostCommonCard();
    public int getMostCommonCardQuantity();
    public Card getLeastFoundCard();
    public int getLeastFoundCardQuantity();
    public List<Card> getUnclaimedCards();
    public List<Card> getLatestCards(int limit);
}
