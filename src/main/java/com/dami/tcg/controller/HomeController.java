package com.dami.tcg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.dami.tcg.modelo.ImplStatsBD;
import com.dami.tcg.modelo.StatsDAO;
import com.dami.tcg.modelo.Card;
import java.util.List;

@Controller
public class HomeController {
    StatsDAO statsDao = new ImplStatsBD();

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

    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("page", "privacy");
        return "footerpages";
    }

    @GetMapping("/terms")
    public String terms(Model model) {
        model.addAttribute("page", "terms");
        return "footerpages";
    }

    @GetMapping("/support")
    public String support(Model model) {
        model.addAttribute("page", "support");
        return "footerpages";
    }
}
