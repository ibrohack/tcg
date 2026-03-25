package com.dami.tcg.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dami.tcg.modelo.*;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

@Controller
public class DeckController {
	DeckDAO dao = new ImplDeckBD();

	public boolean checkCard(Deck deck) {
		return dao.checkCard(deck);
	}

	@GetMapping("/deckcreate")
	public String showDeckCreate(Model model, HttpSession session) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (player == null) {
			return "redirect:/login";
		}
		List<Card> cards = dao.queryPlayerCards(player.getPlayerId());
		Deck deck = new Deck();
		model.addAttribute("deck", deck);
		model.addAttribute("player", player);
		model.addAttribute("cards", cards);
		model.addAttribute("deckQuantity", deck.getCards().size());
		return "deckcreate";
	}

	@PostMapping("/deckcreate")
	public String insertDeck(Model model, RedirectAttributes redirectAttributes,
			@RequestParam String title,
			@RequestParam String description,
			@RequestParam HashMap<Integer, Integer> cards) {

		Deck deck = new Deck(0, title, description, cards);
		boolean created = dao.insertDeck(deck);
		if (created) {
			redirectAttributes.addFlashAttribute("success", "Deck created successfully");
			return "redirect:/player";
		} else {
			redirectAttributes.addFlashAttribute("error", "Error creating deck");
			return "redirect:/deckcreate";
		}
	}

	public boolean deleteDeck(Deck deck) {
		return dao.deleteDeck(deck);
	}

	public boolean updateDeck(Deck deck) {
		return dao.updateDeck(deck);
	}

	@GetMapping("/deckcheck")
	public String deckCheck(Model model, HttpSession session, Deck deck, RedirectAttributes redirectAttributes) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (player == null) {
			return "redirect:/login";
		}
		List<Deck> decks = dao.queryPlayerDecks(player.getPlayerId());
		model.addAttribute("decks", decks);
		model.addAttribute("player", player);
		if (decks.isEmpty()) {
			model.addAttribute("message", "You have no decks to check");
			return "deckcheck";
		} else {
			return "deckcheck";
		}
	}

	public Deck queryDeck(int deckId) {
		return dao.queryDeck(deckId);
	}

}