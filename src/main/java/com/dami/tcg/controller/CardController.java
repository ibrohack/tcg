package com.dami.tcg.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dami.tcg.modelo.*;

import org.springframework.ui.Model;

/**
 * Controller responsible for handling card-related HTTP requests.
 * <p>
 * Provides CRUD operations for {@link Card} entities and exposes an endpoint
 * to display all available cards in the TCG catalogue.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 */
@Controller
public class CardController {
    CardDAO dao = new ImplCardBD();

    /**
     * Checks whether a given card exists in the database.
     *
     * @param card the {@link Card} to verify
     * @return {@code true} if the card exists, {@code false} otherwise
     */
    public boolean checkCard(Card card) {
        return dao.checkCard(card);
    }

    /**
     * Inserts a new card into the database.
     *
     * @param card the {@link Card} to insert
     * @return {@code true} if the insertion was successful, {@code false} otherwise
     */
    public boolean insertCard(Card card) {
        return dao.insertCard(card);
    }

    /**
     * Deletes a card from the database.
     *
     * @param card the {@link Card} to delete
     * @return {@code true} if the deletion was successful, {@code false} otherwise
     */
    public boolean deleteCard(Card card) {
        return dao.deleteCard(card);
    }

    /**
     * Updates an existing card in the database.
     *
     * @param card the {@link Card} with updated information
     * @return {@code true} if the update was successful, {@code false} otherwise
     */
    public boolean updateCard(Card card) {
        return dao.updateCard(card);
    }

    /**
     * Retrieves all cards from the database.
     *
     * @return a {@link List} of all {@link Card} objects
     */
    public List<Card> queryAll() {
        return dao.queryAll();
    }

    /**
     * Retrieves a card by its name.
     *
     * @param name the name of the card to search for
     * @return the matching {@link Card}, or {@code null} if not found
     */
    public Card queryCard(String name) {
        return dao.queryCard(name);
    }

    /**
     * Handles GET requests to {@code /cards} and displays the full card catalogue.
     * <p>
     * Retrieves all cards from the database and adds them to the model
     * for rendering in the {@code cards} view.
     * </p>
     *
     * @param model the {@link Model} used to pass card data to the view
     * @return the name of the view template ({@code "cards"})
     */
    @GetMapping("/cards") // This method is called when the user navigates to the localhost:8080/cards URL
    public String listCards(Model model) {
        List<Card> cards = dao.queryAll();
        model.addAttribute("cards", cards);
        return "cards";
    }
}