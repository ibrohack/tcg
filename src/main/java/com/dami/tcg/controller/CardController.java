package com.dami.tcg.controller;

import java.util.List;

import com.dami.tcg.modelo.*;

public class CardController {
	CardDAO dao = new ImplCardDAO();
	
	public boolean checkCard(Card card) {
		return dao.checkCard(Card card);
	}

    public boolean insertCard(Card card) {
    	return dao.insertCard(Card card);
    }

    public boolean deleteCard(Card card) {
    	return dao.deleteCard(Card card);
    }

    public boolean updateCard(Card card) {
    	return dao.updateCard(Card card);
    }

    public List<Card> queryAll(){
    	return dao.queryAll();
    }

    public Card queryCard(int cardId) {
    	return dao.queryCard(int cardId);
    }
}