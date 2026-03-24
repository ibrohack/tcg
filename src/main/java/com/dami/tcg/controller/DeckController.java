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
		cards.add(new Card(1, "The Card", "Test", "Epic"));
		cards.add(new Card(2, "The Card", "Test", "Legendary"));
		cards.add(new Card(3, "The Card", "Test", "Mythic"));
		cards.add(new Card(4, "The Card", "Test", "Arok"));
		cards.add(new Card(5, "The Card", "Test", "Common"));
		model.addAttribute("deck", new Deck());
		model.addAttribute("player", player);
		model.addAttribute("cards", cards);
		model.addAttribute("deckQuantity", 0);
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
			redirectAttributes.addFlashAttribute("message", "Deck created successfully");
			return "redirect:/player";
		} else {
			redirectAttributes.addFlashAttribute("message", "Error creating deck");
			return "redirect:/deckcreate";
		}
	}

	public boolean deleteDeck(Deck deck) {
		return dao.deleteDeck(deck);
	}

	public boolean updateDeck(Deck deck) {
		return dao.updateDeck(deck);
	}

	/*
	 * @GetMapping("/deckCheck")
	 * public String deckCheck(Model model, Deck deck, RedirectAttributes
	 * redirectAttributes) {
	 * List<Deck> decks = dao.queryAll();
	 * model.addAttribute("decks", decks);
	 * if (model.getAttribute("decks") != null) {
	 * redirectAttributes.addFlashAttribute("message", "Deck checked successfully");
	 * return "redirect:/deckcreate";
	 * } else {
	 * redirectAttributes.addFlashAttribute("message", "Error checking deck");
	 * return "redirect:/deckcreate";
	 * }
	 * }
	 */

	public Deck queryDeck(int deckId) {
		return dao.queryDeck(deckId);
	}

}