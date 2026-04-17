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
 * Controller responsible for handling deck-related HTTP requests.
 * <p>
 * Manages the creation, deletion, viewing, and editing of player decks.
 * All deck operations require the player to be authenticated via the HTTP
 * session.
 * Unauthenticated users are redirected to the login page.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 */
@Controller
public class DeckController {
	DeckDAO dao = new ImplDeckBD();
	PlayerDAO playerDao = new ImplPlayerBD();
	CardDAO cardDao = new ImplCardBD();

	/**
	 * Checks whether a given deck exists in the database.
	 *
	 * @param deck the {@link Deck} to verify
	 * @return {@code true} if the deck exists, {@code false} otherwise
	 */
	public boolean checkCard(Deck deck) {
		return dao.checkCard(deck);
	}

	/**
	 * Handles GET requests to {@code /deckcreate} and displays the deck creation
	 * form.
	 * <p>
	 * Retrieves the logged player's available cards and their quantities
	 * to populate the deck creation view.
	 * </p>
	 *
	 * @param model   the {@link Model} used to pass data to the view
	 * @param session the {@link HttpSession} containing the logged player's
	 *                information
	 * @return the name of the view template ({@code "deckcreate"}), or a redirect
	 *         to
	 *         {@code /login} if the player is not authenticated
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
	 * Handles POST requests to {@code /deckcreate} and processes deck creation.
	 * <p>
	 * Associates the new deck with the authenticated player and persists it
	 * to the database. Displays success or error flash messages accordingly.
	 * </p>
	 *
	 * @param model              the {@link Model} used to pass data to the view
	 * @param redirectAttributes the {@link RedirectAttributes} used to pass flash
	 *                           messages
	 * @param session            the {@link HttpSession} containing the logged
	 *                           player's information
	 * @param deck               the {@link Deck} object bound from the form
	 *                           submission
	 * @return a redirect to {@code /player} on success, {@code /deckcreate} on
	 *         failure,
	 *         or {@code /login} if the player is not authenticated
	 */
	@PostMapping("/deckcreate")
	public String insertDeck(Model model, RedirectAttributes redirectAttributes, HttpSession session,
			@ModelAttribute Deck deck) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (player == null) {
			return "redirect:/login";
		}
		deck.setPlayerID(player.getPlayerId());
		if (deck.getCards().isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Deck must have cards");
			return "redirect:/deckcreate";
		} else {
			boolean created = dao.insertDeck(deck);
			if (created) {
				redirectAttributes.addFlashAttribute("success", "Deck created successfully");
				return "redirect:/player";
			} else {
				redirectAttributes.addFlashAttribute("error", "Error creating deck");
				return "redirect:/deckcreate";
			}
		}
	}

	/**
	 * Handles GET requests to {@code /deckdelete} and deletes a deck by its ID.
	 * <p>
	 * Removes the specified deck from the database and redirects to the deck list
	 * with a success or error flash message.
	 * </p>
	 *
	 * @param model              the {@link Model} used to pass data to the view
	 * @param session            the {@link HttpSession} containing the logged
	 *                           player's information
	 * @param deckId             the ID of the deck to delete
	 * @param redirectAttributes the {@link RedirectAttributes} used to pass flash
	 *                           messages
	 * @return a redirect to {@code /deckcheck}, or {@code /login} if the player is
	 *         not authenticated
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
	 * Handles GET requests to {@code /deckcheck} and displays the player's deck
	 * list.
	 * <p>
	 * Retrieves all decks belonging to the authenticated player. If no decks exist,
	 * a message is displayed to the user.
	 * </p>
	 *
	 * @param model              the {@link Model} used to pass data to the view
	 * @param session            the {@link HttpSession} containing the logged
	 *                           player's information
	 * @param deck               the {@link Deck} object (bound from request
	 *                           parameters)
	 * @param redirectAttributes the {@link RedirectAttributes} used to pass flash
	 *                           messages
	 * @return the name of the view template ({@code "deckcheck"}), or a redirect to
	 *         {@code /login} if the player is not authenticated
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
	 * Handles GET requests to {@code /deckview} and displays the contents of a
	 * specific deck.
	 * <p>
	 * Retrieves the deck by ID and resolves the full card details for each card in
	 * the deck.
	 * If the deck is empty or not found, redirects to the deck list with an error
	 * message.
	 * </p>
	 *
	 * @param model              the {@link Model} used to pass data to the view
	 * @param session            the {@link HttpSession} containing the logged
	 *                           player's information
	 * @param redirectAttributes the {@link RedirectAttributes} used to pass flash
	 *                           messages
	 * @param deckId             the ID of the deck to view
	 * @return the name of the view template ({@code "deckview"}), a redirect to
	 *         {@code /deckcheck}
	 *         if the deck is empty, or {@code /login} if the player is not
	 *         authenticated
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
	 * Handles GET requests to {@code /deckedit} and displays the deck editing form.
	 * <p>
	 * Retrieves the deck by ID and resolves the full card details for editing.
	 * If the deck is empty or not found, redirects to the deck list with an error
	 * message.
	 * </p>
	 *
	 * @param model              the {@link Model} used to pass data to the view
	 * @param session            the {@link HttpSession} containing the logged
	 *                           player's information
	 * @param redirectAttributes the {@link RedirectAttributes} used to pass flash
	 *                           messages
	 * @param deckId             the ID of the deck to edit
	 * @return the name of the view template ({@code "deckedit"}), a redirect to
	 *         {@code /deckcheck}
	 *         if the deck is empty, or {@code /login} if the player is not
	 *         authenticated
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