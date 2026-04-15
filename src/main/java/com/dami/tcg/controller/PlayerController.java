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
 * This controller manages player-related actions such as login, registration, profile editing, and basic CRUD operations. It also handles session management, avatar loading, and communication with the PlayerDAO and DeckDAO.
 * @author Brayan, Asier, Oihan, Adam
 */
@Controller
public class PlayerController {
	PlayerDAO dao = new ImplPlayerBD();
	DeckDAO deckDao = new ImplDeckBD();

	/**
	 * This method checks if the username that is being introduced is already used. 
	 * @param username is the username introduced.
	 * author Asier
	 * @return if the username introduced is already used or not.
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
	 * This method shows the login screen
	 * @param session gives information of the logged player
	 * author Brayan
	 * @return if ther's a logged player redirects the user to the player screen and if ther's not any it shows the login page.
	 */
	@GetMapping("/login")
	public String showLogin(HttpSession session) {
		if (session.getAttribute("loggedPlayer") != null) {
			return "redirect:/player";
		}
		return "login";
	}

	/**
	 * This method checks if the inserted parameters in the login are correct.
	 * @param username is the username introduced by the user.
	 * @param password is the password introduced by the user.
	 * @param session gives information about the logged player.
	 * @param redirectAttributes it adds an error message. 
	 * author Adam
	 * @return If the player login is successful sends the user to the main page and if it's not it stays in the login.
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
	 * This method shows to the users the page register  
	 * @param session gives information about the logged player.
	 * author Oihan
	 * @return if there's a logged player it sends the user to the player screen and if there's a player is shows the register page. 
	 */
	@GetMapping("/register")
	public String showRegister(HttpSession session) {
		if (session.getAttribute("loggedPlayer") != null) {
			return "redirect:/player";
		}
		return "register";
	}

	/**
	 * This method gets the parameter of the register page and checks if they fulfill the parameter required. 
	 * @param username is the username introduced by the user.
	 * @param password is the password introduced by the user.
	 * @param confirmPassword is the password introduced by the user to confirm the password.
	 * @param redirectAttributes is used to add a error message.
	 * author Asier
	 * @return if the parameters introduced don't fulfill the standard to register it shows a error message and and the user stays at the register page but if the parameters fulfill the standard it takes the user to the login page.
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

	
//	@GetMapping("/logout")
//	public String logout(HttpSession session) {
//		session.invalidate();
//		return "redirect:/login";
//	}
//
//	// ==================== EXISTING ENDPOINTS ====================
//
//	
//	@GetMapping("/playerCheck")
//	public String checkPlayer(Model model, Player player) {
//		model.addAttribute("player", player);
//		return "playerCheck";
//	}
//	
//	
//	@GetMapping("/playerInsert")
//	public String insertPlayer(Model model, Player player) {
//		model.addAttribute("player", dao.insertPlayer(player));
//		return "playerInsert";
//	}
//
//	
//	@GetMapping("/playerDelete")
//	public String deletePlayer(Model model, Player player) {
//		model.addAttribute("player", dao.deletePlayer(player));
//		return "playerDelete";
//	}
//
//	
//	@GetMapping("/playerUpdate")
//	public String updatePlayer(Model model, Player player) {
//		model.addAttribute("player", dao.updatePlayer(player));
//		return "playerUpdate";
//	}

	/**
	 * Retrieves a player's profile either by ID or from the logged-in session.
	 * Also loads the player's avatar and associated decks.
	 *
	 * @param model the model used to pass attributes to the view
	 * @param session the current HTTP session
	 * @param playerId optional ID of the player to query
	 * author Brayan
	 * @return the player profile view, or a redirect to login if no player is available
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

	
//	@GetMapping("/playerCards")
//	public String queryPlayerCards(Model model, @RequestParam(defaultValue = "1") int playerID) {
//		model.addAttribute("playerCards", dao.queryPlayerCards(playerID));
//		return "playerCards";
//	}

	// ==================== PROFILE EDIT ENDPOINTS ====================

	/**
	 * Displays the profile edit page for the logged-in player.
	 * @param model the model used to pass attributes to the view
	 * @param session the current HTTP session
	 * author Adam
	 * @return the profile edit view, or a redirect to login if no user is logged in
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
	 * Processes profile edits including password changes and profile picture uploads. Validates password rules and handles file storage for profile images.
	 * @param oldPassword the player's current password
	 * @param newPassword the new password requested
	 * @param confirmPassword confirmation of the new password
	 * @param profilePicture an optional uploaded profile image
	 * @param session the current HTTP session
	 * @param redirectAttributes attributes used to pass success or error messages
	 * author Oihan
	 * @return a redirect back to the profile edit page
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
	 * Attempts to locate and assign an avatar image to the given player. Searches for supported image extensions in the external directory.
	 * @param player the player whose avatar should be populated
	 * author Asier
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