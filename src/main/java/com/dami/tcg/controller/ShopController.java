package com.dami.tcg.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dami.tcg.modelo.Card;
import com.dami.tcg.modelo.ImplCardBD;
import com.dami.tcg.modelo.ImplPlayerBD;
import com.dami.tcg.modelo.Player;
import com.dami.tcg.modelo.PlayerDAO;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.dami.tcg.modelo.CardDAO;

/**
 * Controller responsible for handling all shop-related HTTP requests.
 * <p>
 * Manages the in-game shop, including card pack opening (free and paid),
 * individual card purchases from the Flash Acquisitions shop, and coin
 * bundle purchases with real-money simulation.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 */
@Controller
public class ShopController {
	PlayerDAO playerDao = new ImplPlayerBD();
	CardDAO cardDao = new ImplCardBD();

	/**
	 * Handles GET requests to {@code /pack} and displays the pack opening screen.
	 * <p>
	 * Checks if the player is authenticated and determines the pack price:
	 * free (0 coins) if the player has a free pack available, or 500 coins
	 * otherwise.
	 * </p>
	 *
	 * @param session the {@link HttpSession} used to retrieve the logged player's
	 *                information
	 * @param model   the {@link Model} used to pass pack availability and price to
	 *                the view
	 * @return the name of the view template ({@code "pack"}), or a redirect to
	 *         {@code /login} if the player is not authenticated
	 * @author Brayan
	 */
	@GetMapping("/pack")
	public String showPacks(HttpSession session, Model model) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		int price;
		if (player == null) {
			return "redirect:/login";
		}
		if (!playerDao.checkFreePack(player)) {
			price = 500;
		} else {
			price = 0;
		}
		model.addAttribute("packAvilable", playerDao.checkFreePack(player));
		model.addAttribute("price", price);
		return "pack";
	}

	/**
	 * REST endpoint that returns the current pack price for the logged player.
	 * <p>
	 * Returns 0 if the player has a free pack available, or 500 otherwise.
	 * </p>
	 *
	 * @param session the {@link HttpSession} used to retrieve the logged player's
	 *                information
	 * @return a {@link Map} containing the pack price under the key {@code "price"}
	 */
	@GetMapping("/pack/availability")
	@ResponseBody
	public Map<String, Object> getPackAvailablety(HttpSession session) {

		Player player = (Player) session.getAttribute("loggedPlayer");
		boolean isAvailable = false;
		isAvailable = playerDao.checkFreePack(player);
		return Map.of("packAvailability", isAvailable);
	}

	/**
	 * Handles POST requests to {@code /pack} and processes pack opening.
	 * <p>
	 * Generates 5 random cards with rarity-based probabilities. If the pack is
	 * free,
	 * marks the free pack as used. If paid, deducts 500 coins from the player's
	 * balance.
	 * Each generated card is added to the player's collection.
	 * </p>
	 *
	 * @param model              the {@link Model} used to pass the generated cards
	 *                           to the view
	 * @param session            the {@link HttpSession} used to retrieve and update
	 *                           the logged player
	 * @param redirectAttributes the {@link RedirectAttributes} used to pass error
	 *                           flash messages
	 * @return the name of the view template ({@code "pack"}) with opened cards, a
	 *         redirect to
	 *         {@code /pack} if insufficient coins, or {@code /login} if
	 *         unauthenticated
	 * @author Adam
	 */
	@PostMapping("/pack")
	public String openPack(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		int price;
		if (player == null) {
			return "redirect:/login";
		}
		List<Card> cards = new ArrayList<Card>();
		Card card;
		if (!playerDao.checkFreePack(player)) {
			price = 500;
		} else {
			price = 0;
		}
		model.addAttribute("price", price);
		if (!playerDao.checkFreePack(player)) {
			if (playerDao.getGold(player) >= 500) {
				for (int i = 0; i < 5; i++) {
					card = cardDao.queryRandomCard();
					cards.add(card);
					playerDao.addCard(player, card);
				}
				playerDao.buyPack(player);
				model.addAttribute("cards", cards);
				player.setCoins(playerDao.getGold(player));
				session.setAttribute("loggedPlayer", player);
				return "pack";
			} else {
				redirectAttributes.addFlashAttribute("error", "Not enough coins to buy the pack");
				return "redirect:/pack";
			}
		} else {
			for (int i = 0; i < 5; i++) {
				card = cardDao.queryRandomCard();
				cards.add(card);
				playerDao.addCard(player, card);
			}
			playerDao.freePackOpend(player);
			model.addAttribute("cards", cards);
			session.setAttribute("loggedPlayer", player);
			return "pack";
		}
	}

	/**
	 * Handles GET requests to {@code /shop} and displays the shop page.
	 * <p>
	 * Retrieves the shop cards available for the logged player, their purchase
	 * statuses, the current server time (for countdown timers), and free pack
	 * availability. Unauthenticated users see default random cards.
	 * </p>
	 *
	 * @param model   the {@link Model} used to pass shop data to the view
	 * @param session the {@link HttpSession} used to retrieve the logged player's
	 *                information
	 * @return the name of the view template ({@code "shop"})
	 * @author Oihan
	 */
	@GetMapping("/shop")
	public String showShop(Model model, HttpSession session) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		int playerId = 0;
		boolean isPackAvailable = true;
		HashMap<Integer, Boolean> cardsPurchase = new HashMap<Integer, Boolean>();
		ArrayList<Card> cards = null;
		cards = cardDao.queryShopCards(playerId);
		if (player != null) {
			playerId = player.getPlayerId();
			isPackAvailable = playerDao.checkFreePack(player);
			cards = cardDao.queryShopCards(playerId);
			for (Card c : cards) {
				cardsPurchase.put(c.getCardID(), cardDao.queryPurchasedCard(c, player));
			}
		}
		;
		model.addAttribute("cardsPurchase", cardsPurchase);
		model.addAttribute("cards", cards);
		model.addAttribute("packAvailable", isPackAvailable);
		model.addAttribute("serverTime", System.currentTimeMillis());
		return "shop";
	}

	/**
	 * Handles POST requests to {@code /shopCards} with action {@code buy-card} and
	 * processes
	 * the purchase of an individual card from the shop.
	 * <p>
	 * Verifies the card exists in the player's shop listing, checks that the player
	 * has sufficient coins, deducts the purchase price, adds the card to the
	 * player's
	 * collection, and marks it as purchased in the shop.
	 * </p>
	 *
	 * @param cardId             the ID of the card to purchase
	 * @param model              the {@link Model} used to pass data to the view
	 * @param redirectAttributes the {@link RedirectAttributes} used to pass error
	 *                           flash messages
	 * @param session            the {@link HttpSession} used to retrieve the logged
	 *                           player
	 * @return a redirect to {@code /shop}, or {@code /login} if unauthenticated
	 * @author Asier
	 */
	@PostMapping("/shopCards")
	public String buyCards(@RequestParam int cardId, RedirectAttributes redirectAttributes, HttpSession session) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (player == null) {
			return "redirect:/login";
		}
		Card card = null;
		for (Card c : cardDao.queryShopCards(player.getPlayerId())) {
			if (c.getCardID() == cardId) {
				card = c;
			}
		}

		if (card == null) {
			redirectAttributes.addFlashAttribute("error", "Card not found in the shop");
			return "redirect:/shop";
		}

		if (player.getCoins() >= card.getPurchasePrice()) {
			playerDao.addCoins(player, Math.negateExact(card.getPurchasePrice()));
			player.setCoins(playerDao.getGold(player));
			playerDao.addCard(player, card);
			cardDao.purchaseShopCard(card, player);
		} else {
			redirectAttributes.addFlashAttribute("error", "Not enough coins to buy the card");
			return "redirect:/shop";
		}
		return "redirect:/shop";
	}

	/**
	 * Handles POST requests to {@code /shop} with action {@code buy} and redirects
	 * the player to the coin purchase confirmation page.
	 * <p>
	 * Passes the selected bundle's price and coin amount as redirect attributes.
	 * </p>
	 *
	 * @param price              the real-money price of the coin bundle
	 * @param coins              the amount of in-game coins in the bundle
	 * @param redirectAttributes the {@link RedirectAttributes} used to pass bundle
	 *                           details
	 * @param session            the {@link HttpSession} used to verify
	 *                           authentication
	 * @return a redirect to {@code /confirmPurchase}, or {@code /login} if
	 *         unauthenticated
	 * @author Brayan
	 */
	@PostMapping(value = "/shop", params = "action=buy")
	public String buyCoins(@RequestParam int price, @RequestParam int coins, RedirectAttributes redirectAttributes,
			HttpSession session) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (player == null) {
			return "redirect:/login";
		}
		redirectAttributes.addAttribute("price", price);
		redirectAttributes.addAttribute("coins", coins);
		return "redirect:/confirmPurchase";
	}

	/**
	 * Handles GET requests to {@code /confirmPurchase} and displays the payment
	 * confirmation page.
	 * <p>
	 * Shows the selected bundle's price and coin amount for the player to confirm
	 * before completing the purchase.
	 * </p>
	 *
	 * @param price   the real-money price of the coin bundle
	 * @param coins   the amount of in-game coins in the bundle
	 * @param model   the {@link Model} used to pass bundle details to the view
	 * @param session the {@link HttpSession} used to verify authentication
	 * @return the name of the view template ({@code "confirmPurchase"}), or a
	 *         redirect
	 *         to {@code /login} if unauthenticated
	 * @author Adam
	 */
	@GetMapping("/confirmPurchase")
	public String paymentPage(@RequestParam int price, @RequestParam int coins, Model model, HttpSession session) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		model.addAttribute("price", price);
		model.addAttribute("coins", coins);
		if (player == null) {
			return "redirect:/login";
		}
		return "confirmPurchase";
	}

	/**
	 * Handles POST requests to {@code /confirmPurchase} and completes the coin
	 * purchase.
	 * <p>
	 * Adds the purchased coins to the player's balance and updates the session
	 * with the new coin count.
	 * </p>
	 *
	 * @param coins              the amount of in-game coins to add
	 * @param redirectAttributes the {@link RedirectAttributes} (available for
	 *                           future use)
	 * @param session            the {@link HttpSession} used to retrieve and update
	 *                           the logged player
	 * @return a redirect to {@code /shop}, or {@code /login} if unauthenticated
	 * @author Adam
	 */
	@PostMapping(value = "/confirmPurchase")
	public String buyCoins(@RequestParam int coins, HttpSession session) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		playerDao.addCoins(player, coins);
		player.setCoins(playerDao.getGold(player));
		session.setAttribute("loggedPlayer", player);
		return "redirect:/shop";
	}
}