package com.dami.tcg.controller;

import java.util.List;

import com.dami.tcg.modelo.*;

public class CardController {
	CardDAO dao = new ImplCardBD();
	
	public boolean checkCard(Card card) {
		return dao.checkCard(card);
	}

    public boolean insertCard(Card card) {
    	return dao.insertCard(card);
    }

    public boolean deleteCard(Card card) {
    	return dao.deleteCard(card);
    }

    public boolean updateCard(Card card) {
    	return dao.updateCard(card);
    }

    public List<Card> queryAll(){
    	return dao.queryAll();
    }

    public Card queryCard(String name) {
    	return dao.queryCard(name);
    }
}