package com.dami.tcg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dami.tcg.modelo.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class PlayerController {
    PlayerDAO dao = new ImplPlayerBD();

    // ==================== AUTH ENDPOINTS ====================

    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        Player player = dao.queryPlayerByUsername(username);
        if (player != null && player.getPassword().equals(password)) {
            session.setAttribute("loggedPlayer", player);
            return "redirect:/player";
        }
        redirectAttributes.addFlashAttribute("error", "Invalid username or password");
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {
        if (!password.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match");
            return "redirect:/register";
        }

        // Check if username already exists
        Player existing = dao.queryPlayerByUsername(username);
        if (existing != null) {
            redirectAttributes.addFlashAttribute("error", "Username already taken");
            return "redirect:/register";
        }

        Player newPlayer = new Player();
        newPlayer.setUsername(username);
        newPlayer.setPassword(password);
        newPlayer.setCoins(100);
        boolean success = dao.insertPlayer(newPlayer);

        if (success) {
            redirectAttributes.addFlashAttribute("success", "Account created successfully! Please log in.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Registration failed. Please try again.");
            return "redirect:/register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ==================== EXISTING ENDPOINTS ====================

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
    public String queryPlayer(Model model, HttpSession session,
            @RequestParam(required = false) Integer playerId) {
        Player player;
        if (playerId != null) {
            player = dao.queryPlayer(playerId);
        } else if (session.getAttribute("loggedPlayer") != null) {
            Player loggedPlayer = (Player) session.getAttribute("loggedPlayer");
            player = dao.queryPlayer(loggedPlayer.getPlayerId());
        } else {
            return "redirect:/login";
        }
        model.addAttribute("player", player);
        return "player";
    }

    @GetMapping("/playerCards")
    public String queryPlayerCards(Model model, @RequestParam(defaultValue = "1") int playerID) {
        model.addAttribute("playerCards", dao.queryPlayerCards(playerID));
        return "playerCards";
    }
}