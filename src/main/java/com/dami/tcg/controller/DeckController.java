package com.dami.tcg.controller;

import java.util.ArrayList;
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
	PlayerDAO playerDao = new ImplPlayerBD();
	CardDAO cardDao = new ImplCardBD();

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
	public String insertDeck(Model model, RedirectAttributes redirectAttributes, HttpSession session,
			@ModelAttribute Deck deck) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (player == null) {
			return "redirect:/login";
		}
		deck.setPlayerID(player.getPlayerId());

		boolean created = dao.insertDeck(deck);
		if (created) {
			redirectAttributes.addFlashAttribute("success", "Deck created successfully");
			return "redirect:/player";
		} else {
			redirectAttributes.addFlashAttribute("error", "Error creating deck");
			return "redirect:/deckcreate";
		}
	}

	@GetMapping("/deckdelete")
	public String deckDelete(Model model, HttpSession session, @RequestParam int deckId,
			RedirectAttributes redirectAttributes) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (player == null) {
			return "redirect:/login";
		}
		boolean deleted = dao.deleteDeck(dao.queryDeck(deckId));
		if (deleted) {
			redirectAttributes.addFlashAttribute("success", "Deck deleted successfully");
			return "redirect:/deckcheck";
		} else {
			redirectAttributes.addFlashAttribute("error", "Error deleting deck");
			return "redirect:/deckcheck";
		}
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

	@GetMapping("/deckview")
	public String deckView(Model model, HttpSession session, RedirectAttributes redirectAttributes,
			@RequestParam int deckId) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (player == null) {
			return "redirect:/login";
		}
		Deck deck = dao.queryDeck(deckId);
		if (deck.getCards().isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Deck not found or no cards");
			return "redirect:/deckcheck";
		} else {
			ArrayList<Card> cards = new ArrayList<Card>();
			for (int cardId : deck.getCards().keySet()) {
				cards.add(cardDao.queryCardId(cardId));
			}
			model.addAttribute("cardQuantities", deck.getCards());
			model.addAttribute("deck", deck);
			model.addAttribute("player", player);
			model.addAttribute("cards", cards);
			return "deckview";
		}
	}

	@GetMapping("/deckedit")
	public String deckEdit(Model model, HttpSession session, RedirectAttributes redirectAttributes,
			@RequestParam int deckId) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (player == null) {
			return "redirect:/login";
		}
		Deck deck = dao.queryDeck(deckId);
		if (deck.getCards().isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Deck not found or no cards");
			return "redirect:/deckcheck";
		} else {
			ArrayList<Card> cards = new ArrayList<Card>();
			for (int cardId : deck.getCards().keySet()) {
				cards.add(cardDao.queryCardId(cardId));
			}
			model.addAttribute("cardQuantities", deck.getCards());
			model.addAttribute("deck", deck);
			model.addAttribute("player", player);
			model.addAttribute("cards", cards);
			return "deckedit";
		}
	}

}