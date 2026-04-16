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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dami.tcg.modelo.*;

import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Controller responsible for handling player-related HTTP requests.
 * <p>
 * Manages player authentication (login, registration, logout), profile viewing
 * and editing (including password changes and profile picture uploads),
 * player card inventory, and username availability checks.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 */
@Controller
public class PlayerController {
    PlayerDAO dao = new ImplPlayerBD();
    DeckDAO deckDao = new ImplDeckBD();

    /**
     * REST endpoint that checks whether a username is available for registration.
     * <p>
     * Returns a JSON response with an {@code "available"} boolean field.
     * </p>
     *
     * @param username the username to check
     * @return a {@link Map} containing a single key {@code "available"} with a boolean value
     */
    @GetMapping("/api/check-username")
    @ResponseBody
    public Map<String, Boolean> checkUsername(@RequestParam String username) {
        Map<String, Boolean> response = new HashMap<>();
        if (username == null || username.trim().isEmpty()) {
            response.put("available", false);
            return response;
        }
        Player existing = dao.queryPlayerByUsername(username);
        response.put("available", existing == null);
        return response;
    }

    /**
     * Handles GET requests to {@code /login} and displays the login page.
     * <p>
     * If the player is already authenticated, redirects to the player profile page.
     * </p>
     *
     * @param session the {@link HttpSession} used to check for an existing logged-in player
     * @return the name of the view template ({@code "login"}), or a redirect to {@code /player}
     */
    @GetMapping("/login")
    public String showLogin(HttpSession session) {
        if (session.getAttribute("loggedPlayer") != null) {
            return "redirect:/player";
        }
        return "login";
    }

    /**
     * Handles POST requests to {@code /login} and processes player authentication.
     * <p>
     * Verifies the provided credentials using BCrypt password hashing. On success,
     * stores the player in the session and redirects to the profile page. On failure,
     * displays an error message.
     * </p>
     *
     * @param username           the username submitted in the login form
     * @param password           the password submitted in the login form
     * @param session            the {@link HttpSession} to store the authenticated player
     * @param redirectAttributes the {@link RedirectAttributes} used to pass error flash messages
     * @return a redirect to {@code /player} on success, or {@code /login} on failure
     */
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

    /**
     * Handles GET requests to {@code /register} and displays the registration page.
     * <p>
     * If the player is already authenticated, redirects to the player profile page.
     * </p>
     *
     * @param session the {@link HttpSession} used to check for an existing logged-in player
     * @return the name of the view template ({@code "register"}), or a redirect to {@code /player}
     */
    @GetMapping("/register")
    public String showRegister(HttpSession session) {
        if (session.getAttribute("loggedPlayer") != null) {
            return "redirect:/player";
        }
        return "register";
    }

    /**
     * Handles POST requests to {@code /register} and processes new player registration.
     * <p>
     * Validates that passwords match and meet strength requirements (minimum 8 characters,
     * at least one uppercase, one lowercase, one digit, and one special character).
     * Checks for duplicate usernames. On success, creates the player with 1000 initial coins
     * and a BCrypt-hashed password.
     * </p>
     *
     * @param username           the desired username
     * @param password           the desired password
     * @param confirmPassword    the password confirmation
     * @param redirectAttributes the {@link RedirectAttributes} used to pass flash messages
     * @return a redirect to {@code /login} on success, or {@code /register} on failure
     */
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
            redirectAttributes.addFlashAttribute("error",
                    "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.");
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
        newPlayer.setCoins(1000);
        boolean success = dao.insertPlayer(newPlayer);

        if (success) {
            redirectAttributes.addFlashAttribute("success", "Account created successfully! Please log in.");
            return "redirect:/login";
        } else {
            redirectAttributes.addFlashAttribute("error", "Registration failed. Please try again.");
            return "redirect:/register";
        }
    }

    /**
     * Handles GET requests to {@code /logout} and terminates the player's session.
     *
     * @param session the {@link HttpSession} to invalidate
     * @return a redirect to the login page ({@code /login})
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ==================== EXISTING ENDPOINTS ====================

    /**
     * Handles GET requests to {@code /playerCheck} and displays player verification information.
     *
     * @param model  the {@link Model} used to pass the player data to the view
     * @param player the {@link Player} object bound from request parameters
     * @return the name of the view template ({@code "playerCheck"})
     */
    @GetMapping("/playerCheck")
    public String checkPlayer(Model model, Player player) {
        model.addAttribute("player", player);
        return "playerCheck";
    }

    /**
     * Handles GET requests to {@code /playerInsert} and inserts a new player.
     *
     * @param model  the {@link Model} used to pass the insertion result to the view
     * @param player the {@link Player} object to insert
     * @return the name of the view template ({@code "playerInsert"})
     */
    @GetMapping("/playerInsert")
    public String insertPlayer(Model model, Player player) {
        model.addAttribute("player", dao.insertPlayer(player));
        return "playerInsert";
    }

    /**
     * Handles GET requests to {@code /playerDelete} and deletes a player.
     *
     * @param model  the {@link Model} used to pass the deletion result to the view
     * @param player the {@link Player} object to delete
     * @return the name of the view template ({@code "playerDelete"})
     */
    @GetMapping("/playerDelete")
    public String deletePlayer(Model model, Player player) {
        model.addAttribute("player", dao.deletePlayer(player));
        return "playerDelete";
    }

    /**
     * Handles GET requests to {@code /playerUpdate} and updates a player's information.
     *
     * @param model  the {@link Model} used to pass the update result to the view
     * @param player the {@link Player} object with updated data
     * @return the name of the view template ({@code "playerUpdate"})
     */
    @GetMapping("/playerUpdate")
    public String updatePlayer(Model model, Player player) {
        model.addAttribute("player", dao.updatePlayer(player));
        return "playerUpdate";
    }

    /**
     * Handles GET requests to {@code /player} and displays a player's profile page.
     * <p>
     * If a {@code playerId} parameter is provided, displays that player's profile.
     * Otherwise, displays the currently logged-in player's profile. If no player is
     * authenticated, redirects to the login page.
     * </p>
     *
     * @param model    the {@link Model} used to pass player and deck data to the view
     * @param session  the {@link HttpSession} containing the logged player's information
     * @param playerId optional player ID to view a specific player's profile
     * @return the name of the view template ({@code "player"}), or a redirect to {@code /login}
     */
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
        model.addAttribute("decks", deckDao.queryPlayerDecks(player.getPlayerId()));
        return "player";
    }

    /**
     * Handles GET requests to {@code /playerCards} and displays a player's card inventory.
     *
     * @param model    the {@link Model} used to pass card data to the view
     * @param playerID the ID of the player whose cards to retrieve (defaults to 1)
     * @return the name of the view template ({@code "playerCards"})
     */
    @GetMapping("/playerCards")
    public String queryPlayerCards(Model model, @RequestParam(defaultValue = "1") int playerID) {
        model.addAttribute("playerCards", dao.queryPlayerCards(playerID));
        return "playerCards";
    }

    // ==================== PROFILE EDIT ENDPOINTS ====================

    /**
     * Handles GET requests to {@code /profile/edit} and displays the profile editing form.
     * <p>
     * Retrieves the current player's data and avatar for display in the edit view.
     * </p>
     *
     * @param model   the {@link Model} used to pass player data to the view
     * @param session the {@link HttpSession} containing the logged player's information
     * @return the name of the view template ({@code "profile-edit"}), or a redirect to
     *         {@code /login} if the player is not authenticated
     */
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

    /**
     * Handles POST requests to {@code /profile/edit} and processes profile updates.
     * <p>
     * Supports two types of updates:
     * <ul>
     *   <li><strong>Password change:</strong> Validates the current password, checks that
     *       new passwords match and meet strength requirements, then updates the hash.</li>
     *   <li><strong>Profile picture upload:</strong> Saves the uploaded image to the external
     *       {@code data/images/players} directory using the player's ID as the filename.</li>
     * </ul>
     * </p>
     *
     * @param oldPassword        the current password for verification (optional)
     * @param newPassword        the new password to set (optional)
     * @param confirmPassword    the new password confirmation (optional)
     * @param profilePicture     the uploaded profile picture file (optional)
     * @param session            the {@link HttpSession} containing the logged player's information
     * @param redirectAttributes the {@link RedirectAttributes} used to pass flash messages
     * @return a redirect to {@code /profile/edit}, or {@code /login} if not authenticated
     */
    @PostMapping("/profile/edit")
    public String processProfileEdit(
            @RequestParam(required = false) String oldPassword,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String confirmPassword,
            @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
            HttpSession session, RedirectAttributes redirectAttributes) {

        Player loggedPlayer = (Player) session.getAttribute("loggedPlayer");
        if (loggedPlayer == null)
            return "redirect:/login";

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
                redirectAttributes.addFlashAttribute("error",
                        "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character.");
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
                Path uploadPath = Paths.get("data/images/players");
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Files.copy(profilePicture.getInputStream(), uploadPath.resolve(newFileName),
                        StandardCopyOption.REPLACE_EXISTING);

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

    /**
     * Populates the player's avatar URL by scanning the external images directory
     * for a file matching the player's ID with a supported image extension.
     * <p>
     * Supported extensions: {@code .png}, {@code .jpg}, {@code .jpeg}, {@code .webp}, {@code .gif}.
     * If no matching file is found, the avatar URL is set to {@code null}.
     * </p>
     *
     * @param player the {@link Player} whose avatar URL should be populated
     */
    private void populatePlayerWithAvatar(Player player) {
        if (player == null)
            return;

        player.setAvatarUrl(null); // Clear previous
        String[] extensions = { ".png", ".jpg", ".jpeg", ".webp", ".gif" };

        try {
            // Check external directory 'data/player-images'
            File uploadDir = new File("data/images/players");
            if (uploadDir.exists() && uploadDir.isDirectory()) {
                for (String ext : extensions) {
                    File f = new File(uploadDir, player.getPlayerId() + ext);
                    if (f.exists()) {
                        player.setAvatarUrl("/images/players/" + player.getPlayerId() + ext);
                        return;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to read image files: " + e.getMessage());
        }
    }
}