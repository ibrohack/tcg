package com.dami.tcg.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dami.tcg.modelo.*;

import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import java.util.regex.Pattern;

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
        if (player != null && BCrypt.checkpw(password, player.getPassword())) {
            populatePlayerWithAvatar(player);
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

        // Strong password regex pattern
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_\\-]).{8,}$";
        if (!Pattern.matches(passwordPattern, password)) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.");
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
        newPlayer.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
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
        populatePlayerWithAvatar(player);
        model.addAttribute("player", player);
        return "player";
    }

    @GetMapping("/playerCards")
    public String queryPlayerCards(Model model, @RequestParam(defaultValue = "1") int playerID) {
        model.addAttribute("playerCards", dao.queryPlayerCards(playerID));
        return "playerCards";
    }

    // ==================== PROFILE EDIT ENDPOINTS ====================

    @GetMapping("/profile/edit")
    public String showProfileEdit(Model model, HttpSession session) {
        if (session.getAttribute("loggedPlayer") == null) {
            return "redirect:/login";
        }
        Player loggedPlayer = (Player) session.getAttribute("loggedPlayer");
        Player player = dao.queryPlayer(loggedPlayer.getPlayerId());
        populatePlayerWithAvatar(player);
        model.addAttribute("player", player);
        return "profile-edit";
    }

    @PostMapping("/profile/edit")
    public String processProfileEdit(
            @RequestParam(required = false) String oldPassword,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String confirmPassword,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            HttpSession session, RedirectAttributes redirectAttributes) {

        Player loggedPlayer = (Player) session.getAttribute("loggedPlayer");
        if (loggedPlayer == null) return "redirect:/login";

        Player player = dao.queryPlayer(loggedPlayer.getPlayerId());
        boolean changesMade = false;

        // Password update
        if (oldPassword != null && !oldPassword.isEmpty()) {
            if (!BCrypt.checkpw(oldPassword, player.getPassword())) {
                redirectAttributes.addFlashAttribute("error", "Incorrect current password");
                return "redirect:/profile/edit";
            }
            if (newPassword == null || !newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "New passwords do not match");
                return "redirect:/profile/edit";
            }

            String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_\\-]).{8,}$";
            if (!Pattern.matches(passwordPattern, newPassword)) {
                redirectAttributes.addFlashAttribute("error", "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.");
                return "redirect:/profile/edit";
            }

            player.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            dao.updatePlayer(player);
            changesMade = true;
        }

        // Profile picture upload
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                String originalFilename = profilePicture.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.lastIndexOf(".") > 0) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                String newFileName = player.getPlayerId() + extension;

                // Save to external directory 'data/player-images'
                Path uploadPath = Paths.get("data/player-images");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(profilePicture.getInputStream(), uploadPath.resolve(newFileName), StandardCopyOption.REPLACE_EXISTING);

                changesMade = true;
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Failed to upload profile picture: " + e.getMessage());
                return "redirect:/profile/edit";
            }
        }

        if (changesMade) {
            populatePlayerWithAvatar(player);
            session.setAttribute("loggedPlayer", player);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        }

        return "redirect:/profile/edit";
    }

    private void populatePlayerWithAvatar(Player player) {
        if (player == null) return;
        
        player.setAvatarUrl(null); // Clear previous
        String[] extensions = {".png", ".jpg", ".jpeg", ".webp", ".gif"};
        
        try {
            // Check external directory 'data/player-images'
            File uploadDir = new File("data/player-images");
            if (uploadDir.exists() && uploadDir.isDirectory()) {
                for (String ext : extensions) {
                    File f = new File(uploadDir, player.getPlayerId() + ext);
                    if (f.exists()) {
                        player.setAvatarUrl("/player-images/" + player.getPlayerId() + ext);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to read image files: " + e.getMessage());
        }
    }
}