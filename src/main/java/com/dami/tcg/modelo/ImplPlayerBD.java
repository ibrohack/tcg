package com.dami.tcg.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ImplPlayerBD implements PlayerDAO {
	// Attributes
	private Connection connection;
	private PreparedStatement statement;

	// The following attributes are used to collect the values from the
	// configuration file
	private ResourceBundle configFile;
	private String driverBD;
	private String urlBD;
	private String userBD;
	private String passwordBD;

	// SQL Statements
	final String SQLSELECT = "SELECT * FROM Players WHERE PlayerId = ?";
	final String SQLINSERT = "INSERT INTO Players (Username, PlayerPassword, Coins) VALUES (?,?,?)";
	final String SQLCONSULTA = "SELECT * FROM Players";
	final String SQLBORRAR = "DELETE FROM Players WHERE PlayerId=?";
	final String SQLMODIFICAR = "UPDATE Players SET PlayerPassword=? WHERE PlayerId=?";
	final String SQLSELECTCARDS = "SELECT * FROM PlayersCards WHERE PlayerId=?";
	final String SQLSELECTBYUSERNAME = "SELECT * FROM Players WHERE Username = ?";
	final String SQLUPDATECARDQUANTITY = "UPDATE PlayersCards SET Quantity = ? WHERE PlayerID = ? AND CardID = ?";
	final String SQLADDCARD = "INSERT INTO PlayersCards VALUES (?,?,1)";
	final String SQLGETGOLD = "SELECT Coins FROM Players WHERE PlayerId=?";
	final String SQLUPDTEGOLD = "UPDATE Players SET Coins = ? WHERE PlayerId=?";
	final String SQLGETCARDQUANTITY = "SELECT Quantity FROM PlayersCards WHERE PlayerID = ? AND CardID = ?";


	public ImplPlayerBD() {
		this.configFile = ResourceBundle.getBundle("configDB");
		this.driverBD = this.configFile.getString("Driver");
		this.urlBD = this.configFile.getString("Conn");
		this.userBD = this.configFile.getString("DBUser");
		this.passwordBD = this.configFile.getString("DBPass");
	}

	private void openConnection() {
		try {
			Class.forName(this.driverBD);
			connection = DriverManager.getConnection(urlBD, this.userBD, this.passwordBD);
		} catch (ClassNotFoundException e) {
			System.out.println("Error loading driver");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Error opening database");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean checkPlayer(Player player) {
		boolean existe = false;
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLSELECT);
			statement.setInt(1, player.getPlayerId());
			ResultSet resultado = statement.executeQuery();

			if (resultado.next()) {
				existe = true;
			}
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error verifying player: " + e.getMessage());
		}
		return existe;
	}

	@Override
	public boolean insertPlayer(Player player) {
		boolean ok = false;
		if (!checkPlayer(player)) {
			this.openConnection();
			try {
				statement = connection.prepareStatement(SQLINSERT);
				statement.setString(1, player.getUsername());
				statement.setString(2, player.getPassword());
				statement.setInt(3, player.getCoins());
				if (statement.executeUpdate() > 0) {
					ok = true;
				}
				statement.close();
				connection.close();
			} catch (SQLException e) {
				System.out.println("Error inserting player: " + e.getMessage());
			}
		}
		return ok;
	}

	@Override
	public boolean deletePlayer(Player player) {
		boolean ok = false;
		if (checkPlayer(player)) {
			this.openConnection();
			try {
				statement = connection.prepareStatement(SQLBORRAR);
				statement.setInt(1, player.getPlayerId());
				if (statement.executeUpdate() > 0) {
					ok = true;
				}
				statement.close();
				connection.close();
			} catch (SQLException e) {
				System.out.println("Error deleting player: " + e.getMessage());
			}
		}
		return ok;
	}

	@Override
	public boolean updatePlayer(Player player) {
		boolean ok = false;
		if (checkPlayer(player)) {
			this.openConnection();
			try {
				statement = connection.prepareStatement(SQLMODIFICAR);
				statement.setString(1, player.getPassword());
				statement.setInt(2, player.getPlayerId());
				if (statement.executeUpdate() > 0) {
					ok = true;
				}
				statement.close();
				connection.close();
			} catch (SQLException e) {
				System.out.println("Error updating player: " + e.getMessage());
			}
		}
		return ok;
	}

	@Override
	public Player queryPlayer(int playerId) {
		Player player = null;
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLSELECT);
			statement.setInt(1, playerId);
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()) {
				player = new Player(
						resultado.getInt("PlayerId"),
						resultado.getString("Username"),
						resultado.getString("PlayerPassword"),
						resultado.getInt("Coins"),
						queryPlayerCards(playerId),
						null);
			}
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error getting player by ID: " + e.getMessage());
		}
		return player;
	}

	@Override
	public HashMap<Integer, Integer> queryPlayerCards(int playerID) {
		HashMap<Integer, Integer> cards = new HashMap<Integer, Integer>();
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLSELECTCARDS);
			statement.setInt(1, playerID);
			ResultSet resultado = statement.executeQuery();
			while (resultado.next()) {
				cards.put(resultado.getInt("CardID"), resultado.getInt("Quantity"));
			}
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error getting cards of the player: " + e.getMessage());
		}
		return cards;
	}

	@Override
	public Player queryPlayerByUsername(String username) {
		Player player = null;
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLSELECTBYUSERNAME);
			statement.setString(1, username);
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()) {
				int playerId = resultado.getInt("PlayerId");
				player = new Player(
						playerId,
						resultado.getString("Username"),
						resultado.getString("PlayerPassword"),
						resultado.getInt("Coins"),
						queryPlayerCards(playerId),
						null);
			}
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error getting player by username: " + e.getMessage());
		}
		return player;
	}

	public int queryCardQuantity(Player player, Card card) {
		int quantity=0;
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLGETCARDQUANTITY);
			statement.setInt(2, player.getPlayerId());
			statement.setInt(3, card.getCardID());
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()) {
				quantity = resultado.getInt("Quantity");
			}
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return quantity;
	}

	@Override
	public boolean addCard(Player player, Card card) {
		boolean ok = false;
		HashMap<Integer, Integer> cards = queryPlayerCards(player.getPlayerId());
		int quantity = this.queryCardQuantity(player, card);
		this.openConnection();
		try {
			if (cards.containsKey(card.getCardID())) {
				statement = connection.prepareStatement(SQLUPDATECARDQUANTITY);
				statement.setInt(1, quantity+1);
				statement.setInt(2, player.getPlayerId());
				statement.setInt(3, card.getCardID());
				if (statement.executeUpdate() > 0) {
					ok = true;
				}
			} else {
				statement = connection.prepareStatement(SQLADDCARD);
				statement.setInt(1, player.getPlayerId());
				statement.setInt(2, card.getCardID());
				if (statement.executeUpdate() > 0) {
					ok = true;
				}
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ok;
	}

	@Override
	public void buyPack(Player player) {
		boolean ok = false;
		int coins = this.getGold(player);
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLGETGOLD);
			statement.setInt(1, player.getPlayerId());
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()) {
				coins = resultado.getInt("Coins");
			}
			statement.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (coins >= 500) {
			try {
				statement = connection.prepareStatement(SQLUPDTEGOLD);
				statement.setInt(1, coins - 500);
				statement.setInt(2, player.getPlayerId());
				if (statement.executeUpdate() > 0) {
					ok = true;
				}
				statement.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public int getGold(Player player) {
		int coins = 0;
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLGETGOLD);
			statement.setInt(1, player.getPlayerId());
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()) {
				coins = resultado.getInt("Coins");
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return coins;
	}

}