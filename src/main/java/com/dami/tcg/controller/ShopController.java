package com.dami.tcg.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.dami.tcg.modelo.Card;
import com.dami.tcg.modelo.ImplCardBD;
import com.dami.tcg.modelo.ImplPlayerBD;
import com.dami.tcg.modelo.PlayerDAO;
import com.dami.tcg.modelo.CardDAO;

@Controller
public class ShopController {
	PlayerDAO playerDao = new ImplPlayerBD();
	CardDAO cardDao = new ImplCardBD();

	@GetMapping("/pack")
	public String showPacks() {
		return "pack";
	}

	@PostMapping("/pack")
	public String openPack(){
		List<Card> cards = null;
		Card card;
		for(int i = 0; i<6; i++) {
			card = cardDao.queryRandomCard();
			cards.add(card);
			playerDao.addCard(null, card);
		}
		return "pack";
	}

}
