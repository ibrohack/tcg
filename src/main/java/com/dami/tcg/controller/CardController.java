package com.dami.tcg.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dami.tcg.modelo.*;

import org.springframework.ui.Model;

@Controller
public class CardController {
    CardDAO dao = new ImplCardBD();

    public boolean checkCard(Card card) {
        return dao.checkCard(card);
    }

    public boolean insertCard(Card card) {
        return dao.insertCard(card);
    }

    public boolean deleteCard(Card card) {
        return dao.deleteCard(card);
    }

    public boolean updateCard(Card card) {
        return dao.updateCard(card);
    }

    public List<Card> queryAll() {
        return dao.queryAll();
    }

    public Card queryCard(String name) {
        return dao.queryCard(name);
    }

    @GetMapping("/cards")
    public String listCards(Model model) {
        Card card1 = new Card(1, "Goku", "Legendary", "Card of Goku");
        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        model.addAttribute("cards", cards);
        return "cards";
    }
}