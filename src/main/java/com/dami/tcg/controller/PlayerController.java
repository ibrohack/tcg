package com.dami.tcg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.dami.tcg.modelo.*;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PlayerController {
    PlayerDAO dao = new ImplPlayerBD();

    @GetMapping("/playerCheck")
    public String checkPlayer(Model model, Player player) {
        model.addAttribute("player", player);
        return "playerCheck";
    }

    @GetMapping("/playerInsert")
    public String insertPlayer(Model model, Player player) {
        model.addAttribute("player", dao.insertPlayer(player));
        return "playerInsert";
    }

    @GetMapping("/playerDelete")
    public String deletePlayer(Model model, Player player) {
        model.addAttribute("player", dao.deletePlayer(player));
        return "playerDelete";
    }

    @GetMapping("/playerUpdate")
    public String updatePlayer(Model model, Player player) {
        model.addAttribute("player", dao.updatePlayer(player));
        return "playerUpdate";
    }

    @GetMapping("/player")
    public String queryPlayer(Model model, @RequestParam(defaultValue = "1") int playerId) {
        Player p = dao.queryPlayer(playerId);
        model.addAttribute("player", p);
        return "player";
    }

    @GetMapping("/playerCards")
    public String queryPlayerCards(Model model, @RequestParam(defaultValue = "1") int playerID) {
        model.addAttribute("playerCards", dao.queryPlayerCards(playerID));
        return "playerCards";
    }
}