package com.dami.tcg.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dami.tcg.modelo.Card;
import com.dami.tcg.modelo.ImplCardBD;
import com.dami.tcg.modelo.ImplPlayerBD;
import com.dami.tcg.modelo.Player;
import com.dami.tcg.modelo.PlayerDAO;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.dami.tcg.modelo.CardDAO;

@Controller
public class ShopController {
	PlayerDAO playerDao = new ImplPlayerBD();
	CardDAO cardDao = new ImplCardBD();

	@GetMapping("/pack")
	public String showPacks(HttpSession session) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (player == null) {
			return "redirect:/login";
		}
		return "pack";
	}

	@PostMapping("/pack")
	public String openPack(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (player == null) {
			return "redirect:/login";
		}
		List<Card> cards = new ArrayList<Card>();
		;
		Card card;
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

	}

	@GetMapping("/shop")
	public String showShop(Model model, HttpSession session) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		int playerId = (player != null) ? player.getPlayerId() : 0;
		ArrayList<Card> cards = cardDao.queryShopCards(playerId);
		model.addAttribute("cards", cards);
		model.addAttribute("serverTime", System.currentTimeMillis());
		return "shop";
	}

	@PostMapping(value = "/shopCards", params = "action=buy-card")
	public String buyCards(@RequestParam int cardId, RedirectAttributes redirectAttributes, HttpSession session) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		Card card = null;
		for(Card c : cardDao.queryShopCards(player.getPlayerId())) {
			if(c.getCardID()==cardId) {
				card=c;
			}
		}	
		if (player.getCoins() >= card.getPurchasePrice()) {
			playerDao.addCoins(player, Math.negateExact(card.getPurchasePrice()));
			player.setCoins(playerDao.getGold(player));
			playerDao.addCard(player, card);
		}else{
			redirectAttributes.addFlashAttribute("error", "Not enough coins to buy the card");
			return "redirect:/shop";
		}
		return "redirect:/";
	}

	@PostMapping(value = "/shop", params = "action=buy")
	public String buyCoins(@RequestParam int price, @RequestParam int coins, RedirectAttributes redirectAttributes, HttpSession session) {
		Player player = (Player) session.getAttribute("loggedPlayer");
		if (player == null) {
			return "redirect:/login";
		}
		redirectAttributes.addAttribute("price", price);
		redirectAttributes.addAttribute("coins", coins);
		return "redirect:/confirmPurchase";
	}

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
