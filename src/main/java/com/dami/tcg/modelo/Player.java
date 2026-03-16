package com.dami.tcg.modelo;

import java.util.ArrayList;
import java.util.HashMap;

public class Player {
	private int playerId;
	private String username;
	private String passwore;
	private HashMap<Integer, Integer> cards;
	private ArrayList<Deck> decks;
	
	public final HashMap<Integer, Integer> getCards() {
		return cards;
	}
	public final void setCards(HashMap<Integer, Integer> cards) {
		this.cards = cards;
	}
	public final ArrayList<Deck> getDecks() {
		return decks;
	}
	public final void setDecks(ArrayList<Deck> decks) {
		this.decks = decks;
	}
	public final int getPlayerId() {
		return playerId;
	}
	public final void setPlayerId(int playerId) {
		this.playerId = playerId;
	}
	public final String getUsername() {
		return username;
	}
	public final void setUsername(String username) {
		this.username = username;
	}
	public final String getPasswore() {
		return passwore;
	}
	public final void setPasswore(String passwore) {
		this.passwore = passwore;
	}
	
	public Player(int playerId, String username, String passwore, HashMap<Integer, Integer> cards, ArrayList<Deck> decks) {
		super();
		this.playerId = playerId;
		this.username = username;
		this.passwore = passwore;
		this.cards = cards;
		this.decks = decks;
	}
	public Player() {
		super();
	}
	@Override
	public String toString() {
		return "Player [playerId=" + playerId + ", username=" + username + ", passwore=" + passwore + ", cards=" + cards
				+ ", decks=" + decks + "]";
	}
	
	
}
