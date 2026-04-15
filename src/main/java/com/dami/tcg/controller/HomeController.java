package com.dami.tcg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.dami.tcg.modelo.ImplStatsBD;
import com.dami.tcg.modelo.StatsDAO;
import com.dami.tcg.modelo.Card;
import java.util.List;

/**
 * This controller handles the home page of the application. It retrieves general statistics such as active players, card rarity distribution, unclaimed cards, and recently added cards, and sends them to the view.
 * @author Brayan
 */
@Controller
public class HomeController {
    StatsDAO statsDao = new ImplStatsBD();
    /**
     * Loads general game statistics and sends them to the home page. This includes active player count, most and least common cards, unclaimed cards, and the latest added cards.
     * @param model used to pass statistics and card data to the view
     * author Brayan
     * @return the home page view
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
}
