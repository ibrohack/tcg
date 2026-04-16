package com.dami.tcg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.dami.tcg.modelo.ImplStatsBD;
import com.dami.tcg.modelo.StatsDAO;
import com.dami.tcg.modelo.Card;
import java.util.List;

/**
 * Controller responsible for handling the home page and static informational pages.
 * <p>
 * Serves the main dashboard with live statistics (active players, most common cards,
 * least found cards, unclaimed cards, and latest releases), as well as footer pages
 * such as privacy policy, terms of service, and support.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 */
@Controller
public class HomeController {
    StatsDAO statsDao = new ImplStatsBD();

    /**
     * Handles GET requests to the root URL ({@code /}) and displays the home page dashboard.
     * <p>
     * Retrieves various game statistics from the database and populates the model
     * for display, including active player count, most common and least found cards,
     * unclaimed cards, and the latest card releases.
     * </p>
     *
     * @param model the {@link Model} used to pass statistics data to the view
     * @return the name of the view template ({@code "home"})
     */
    @GetMapping("/")
    public String home(Model model) {
        int activePlayersCount = statsDao.getActivePlayersCount();
        Card mostCommonCard = statsDao.getMostCommonCard();
        int mostCommonQty = statsDao.getMostCommonCardQuantity();
        Card leastFoundCard = statsDao.getLeastFoundCard();
        int leastFoundQty = statsDao.getLeastFoundCardQuantity();
        List<Card> unclaimedCards = statsDao.getUnclaimedCards();
        List<Card> latestCards = statsDao.getLatestCards(4);

        model.addAttribute("activePlayersCount", activePlayersCount);
        model.addAttribute("mostCommonCard", mostCommonCard);
        model.addAttribute("mostCommonQty", mostCommonQty);
        model.addAttribute("leastFoundCard", leastFoundCard);
        model.addAttribute("leastFoundQty", leastFoundQty);
        
        if (unclaimedCards != null && !unclaimedCards.isEmpty()) {
            model.addAttribute("unclaimedCard", unclaimedCards.get(0));
            model.addAttribute("hasUnclaimed", true);
        } else {
            model.addAttribute("hasUnclaimed", false);
        }
        
        model.addAttribute("latestCards", latestCards);

        return "home";
    }

    /**
     * Handles GET requests to {@code /privacy} and displays the privacy policy page.
     *
     * @param model the {@link Model} used to pass the page identifier to the view
     * @return the name of the view template ({@code "footerpages"})
     */
    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("page", "privacy");
        return "footerpages";
    }

    /**
     * Handles GET requests to {@code /terms} and displays the terms of service page.
     *
     * @param model the {@link Model} used to pass the page identifier to the view
     * @return the name of the view template ({@code "footerpages"})
     */
    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("page", "terms");
        return "footerpages";
    }

    /**
     * Handles GET requests to {@code /support} and displays the support page.
     *
     * @param model the {@link Model} used to pass the page identifier to the view
     * @return the name of the view template ({@code "footerpages"})
     */
    @GetMapping("/support")
    public String support(Model model) {
        model.addAttribute("page", "support");
        return "footerpages";
    }
}
