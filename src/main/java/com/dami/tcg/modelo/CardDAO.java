package com.dami.tcg.modelo;

import java.util.ArrayList;
import java.util.List;

public interface CardDAO {
    public boolean checkCard(Card card);

    public boolean insertCard(Card card);

    public boolean deleteCard(Card card);

    public boolean updateCard(Card card);

    public List<Card> queryAll();

    public Card queryCard(String name);

    public Card queryRandomCard();

    public Card queryCardId(int cardId);

    public ArrayList<Card> queryShopCards(int playerId);
}
