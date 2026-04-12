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
		List<Card> cards = new ArrayList<Card>();
		;
		Card card;
		Player player = (Player) session.getAttribute("loggedPlayer");
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
		if (player == null) {
			return "redirect:/login";
		}
		ArrayList<Card> cards = cardDao.queryShopCards(player.getPlayerId());
		model.addAttribute("cards", cards);
		return "shop";
	}

	@PostMapping(value = "/shop", params = "action=buy")
	public String buyCoins(@RequestParam int price, @RequestParam int coins, RedirectAttributes redirectAttributes) {
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
		redirectAttributes.addAttribute("coins", coins);
		playerDao.addCoins(player, coins);
		player.setCoins(playerDao.getGold(player));
		session.setAttribute("loggedPlayer", player);
		return "redirect:/shop";
	}
}
