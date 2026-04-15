package com.dami.tcg.controller;

import java.util.ArrayList;
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
 * This class works as a controller for all the methods related to the shop.
 * @author Brayan, Adam, Oihan and Asier
 */
@Controller
public class ShopController {
	PlayerDAO playerDao = new ImplPlayerBD();
	CardDAO cardDao = new ImplCardBD();

	/**
	 * This method is used to show the pack opening screen, it also checks if there is a player logged and if there's no player logged it returns the user to the login screen. Finally, it sets the price of the pack if the player has the free pack. 
	 * @param session is used to get the logged player's information.
	 * @param model is used to add attributes to the html code like the price of the pack.
	 * @author Brayan
	 * @return if the player is not logged returns to the login screen and if it's logged it redirects to the pack screen.
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

	@GetMapping("/pack/price")
	@ResponseBody
	public Map<String, Object> getPackPrice(HttpSession session) {
		int price;
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (!playerDao.checkFreePack(player)) {
			price = 500;
		} else {
			price = 0;
		}
		return Map.of("price", price);
	}

	/**
	 * This method is called when clicking the the open pack button and it generates 5 random cards with set chances depending the cards rarity. 
	 * @param model is used to add parameters to the html.
	 * @param session is used to get the logged player's information.
	 * @param redirectAttributes is used add and show error messages.  
	 * @author Adam
	 * @return if the player isn't logged it redirects to the loging page and if the player is logged it sends the user to the pack opening screen
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
//				playerDao.buyPack(player);
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
	 * This method shows the webs shop screen it also gets the cards that each player has in the shop, the current time and if the players have the free pack available and it sends them to the html to show them.
	 * @param model sends if the player has the free pack available, the cards  to the html. 
	 * @param session gets the information of the logged player.
	 * @author Oihan
	 * @return it sends the users to the shop.
	 */
	@GetMapping("/shop")
	public String showShop(Model model, HttpSession session) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		int playerId = 0;
		boolean isPackAvailable = true;
		ArrayList<Card> cards = null;
		if (player != null) {
			playerId = player.getPlayerId();
			isPackAvailable = playerDao.checkFreePack(player);
		}
		cards = cardDao.queryShopCards(playerId);
		model.addAttribute("cards", cards);
		model.addAttribute("packAvailable", isPackAvailable);
		model.addAttribute("serverTime", System.currentTimeMillis());
		return "shop";
	}

	/**
	 * This method checks if there's a player logged and if theres's any when clicking at each cards button it adds the card to the player and disables that button.
	 * @param cardId is the Id of the card the user has selected.
	 * @param redirectAttributes is used add and show error messages.  
	 * @param session gets the information of the logged player.
	 * @author Asier
	 * @return if there's no player logged it sends to the login section and if there's a logged player it stays at the shop.
	 */
	@PostMapping(value = "/shopCards", params = "action=buy-card")
	public String buyCards(@RequestParam int cardId, RedirectAttributes redirectAttributes, HttpSession session) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (player == null) {
			return "redirect:/login";
		}
		Card card = null;
		for (Card c : cardDao.queryShopCards(player.getPlayerId())) {
			if (c.getCardID() == cardId) {
				card = c;
				break;
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
		} else {
			redirectAttributes.addFlashAttribute("error", "Not enough coins to buy the card");
			return "redirect:/shop";
		}
		return "redirect:/";
	}

	/**
	 * This method checks if there's a player logged and if there's any when clicking at the bundle it sends the player to the a screen to confirm the purchase of coins.
	 * @param price is the price of the coin bundle selected.
	 * @param coins is the amount of coins for the bundle selected.
	 * @param redirectAttributes is used to send the price and the amount of gold of the bundle selected as attributes when redirecting to the confirmPurchase screen.
	 * @param session gets the information of the logged player.
	 * @author Brayan
	 * @return it sends the user to the screen to confirm the coins purchase.
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
	 * 
	 * @param price
	 * @param coins
	 * @param model
	 * @param session
	 * @author Adam
	 * @return
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
	 * This method shows a new window to confirm the purchase of coins.
	 * @param price is the price of the coin bundle
	 * @param coins is the amount of coins that the bundle selected has.
	 * @param model sends the price of the bundle and the amount of coins to the html. 
	 * @param session gets the information of the logged player.
	 * @author Adam
	 * @return if theres no logged player it sends the user to the login page and if there's any it shows the confirm purchase page.
	 */
	@PostMapping(value = "/confirmPurchase")
	public String buyCoins(@RequestParam int coins, RedirectAttributes redirectAttributes, HttpSession session) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (player == null) {
			return "redirect:/login";
		}
		redirectAttributes.addAttribute("coins", coins);
		playerDao.addCoins(player, coins);
		player.setCoins(playerDao.getGold(player));
		session.setAttribute("loggedPlayer", player);
		return "redirect:/shop";
	}
}
