package com.dami.tcg.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dami.tcg.modelo.*;

import org.springframework.ui.Model;

/**
 * This controller handles the display of all cards in the game. It retrieves every card from the database and sends them to the view for listing.
 * @author Brayan
 */
@Controller
public class CardController {
    CardDAO dao = new ImplCardBD();

    /**
     * Retrieves all cards from the database and sends them to the view to be displayed in a list.
     * @param model used to pass the list of cards to the view
     * @return the cards page
     * author Brayan
     */

    @GetMapping("/cards") 
    public String listCards(Model model) {
        List<Card> cards = dao.queryAll();
        model.addAttribute("cards", cards);
        return "cards";
    }
}