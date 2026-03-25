package com.dami.tcg.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
	public String openPack(Model model, HttpSession session){
		List<Card> cards = new ArrayList<Card>();;
		Card card;
		Player player = (Player) session.getAttribute("loggedPlayer");
		for(int i = 0; i<5; i++) {
			card = cardDao.queryRandomCard();
			cards.add(card);
			playerDao.addCard(player, card);
		}
		playerDao.buyPack(player);
		model.addAttribute("cards",cards);
		return "pack";
	}
	
	@GetMapping("/shop")
	public String showShop(Model model) {
		ArrayList<Card> cards = new ArrayList<Card>();
		Card card;
		for(int i = 0; i<3; i++) {
			card = cardDao.queryRandomCard();
			while(i<0 && cards.contains(card)) {
				card = cardDao.queryRandomCard();
			}
			cards.add(card);
			model.addAttribute("cards",cards);
		}
		return "shop";
	}

}
