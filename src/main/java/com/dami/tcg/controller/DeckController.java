package com.dami.tcg.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dami.tcg.modelo.*;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

/**
 * This controller manages all deck-related actions, including creating,
 * deleting, viewing, and editing decks. It interacts with the DeckDAO,
 * PlayerDAO, and CardDAO to retrieve and update deck and card data.
 * 
 * @author Adan, Oihan, Asier, Brayan
 */
@Controller
public class DeckController {
	DeckDAO dao = new ImplDeckBD();
	PlayerDAO playerDao = new ImplPlayerBD();
	CardDAO cardDao = new ImplCardBD();

	/**
	 * Displays the deck creation page, loading the player's cards and preparing an
	 * empty deck structure for editing.
	 * 
	 * @param model   sends data to the view
	 * @param session retrieves the logged player's information
	 * @return the deck creation page or a redirect to login if not logged in
	 *         author Adam
	 */
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
		model.addAttribute("playerCardQuantities", playerDao.queryPlayerCards(player.getPlayerId()));
		model.addAttribute("deckQuantity", deck.getCards().size());
		return "deckcreate";
	}

	/**
	 * Inserts a new deck for the logged player and redirects depending on whether
	 * the creation was successful.
	 * 
	 * @param model              sends data to the view
	 * @param redirectAttributes used to show success or error messages
	 * @param session            retrieves the logged player's information
	 * @param deck               the deck submitted from the form
	 * @return redirect to the player page or back to deck creation on error
	 *         author Adam
	 */
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

	/**
	 * Deletes a deck by its ID and redirects to the deck list with a success or
	 * error message.
	 * 
	 * @param model              sends data to the view
	 * @param session            retrieves the logged player's information
	 * @param deckId             the ID of the deck to delete
	 * @param redirectAttributes used to show messages
	 * @return redirect to the deck check page or login if not logged in
	 *         author Brayan
	 */
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

	/**
	 * Displays all decks belonging to the logged player. If no decks exist, a
	 * message is shown.
	 * 
	 * @param model              sends data to the view
	 * @param session            retrieves the logged player's information
	 * @param deck               unused deck parameter for binding
	 * @param redirectAttributes used to show messages
	 * @return the deck check page or a redirect to login if not logged in
	 *         author Asier
	 */
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

	/**
	 * Displays the details of a specific deck, including its cards and quantities.
	 * Redirects if the deck is empty or invalid.
	 * 
	 * @param model              sends data to the view
	 * @param session            retrieves the logged player's information
	 * @param redirectAttributes used to show error messages
	 * @param deckId             the ID of the deck to view
	 * @return the deck view page or a redirect if the deck is invalid
	 *         author Oihan
	 */
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

	/**
	 * Loads a deck for editing, including its cards and quantities. Redirects if
	 * the deck is empty or invalid.
	 * 
	 * @param model              sends data to the view
	 * @param session            retrieves the logged player's information
	 * @param redirectAttributes used to show error messages
	 * @param deckId             the ID of the deck to edit
	 * @return the deck edit page or a redirect if the deck is invalid
	 *         author Asier
	 */
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