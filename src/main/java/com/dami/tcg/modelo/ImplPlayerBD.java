package com.dami.tcg.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

/**
 * JDBC-based implementation of the {@link PlayerDAO} interface.
 * <p>
 * Provides concrete database operations for {@link Player} entities using
 * direct JDBC connections. Handles player authentication, card inventory management,
 * coin transactions, and free pack availability. Database connection parameters are
 * loaded from the {@code configDB.properties} resource bundle.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 * @see PlayerDAO
 */
public class ImplPlayerBD implements PlayerDAO {
	// Attributes
	/** The active JDBC connection to the database. */
	private Connection connection;

	/** The prepared statement used for executing SQL queries. */
	private PreparedStatement statement;

	// The following attributes are used to collect the values from the
	// configuration file
	/** The resource bundle containing database configuration properties. */
	private ResourceBundle configFile;

	/** The JDBC driver class name. */
	private String driverBD;

	/** The JDBC connection URL. */
	private String urlBD;

	/** The database username. */
	private String userBD;

	/** The database password. */
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
	final String SQLCHECKFREEPACK = "SELECT PackAvilable FROM Players WHERE PlayerId=?";
	final String SQLUPDATEFREEPACK = "UPDATE Players SET PackAvilable = 0 WHERE PlayerId=?";
	final String SQLSELECTDECK = "SELECT * FROM Decks WHERE DeckID = ?";

	/**
	 * Constructs a new {@code ImplPlayerBD} instance and loads database configuration
	 * from the {@code configDB.properties} resource bundle.
	 */
	public ImplPlayerBD() {
		this.configFile = ResourceBundle.getBundle("configDB");
		this.driverBD = this.configFile.getString("Driver");
		this.urlBD = this.configFile.getString("Conn");
		this.userBD = this.configFile.getString("DBUser");
		this.passwordBD = this.configFile.getString("DBPass");
	}

	/**
	 * Opens a JDBC connection to the database using the configured driver and credentials.
	 */
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

	/**
	 * {@inheritDoc}
	 * <p>
	 * Checks for the existence of the player by querying the database using the player's ID.
	 * </p>
	 */
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

	/**
	 * {@inheritDoc}
	 * <p>
	 * Inserts the player with their username, hashed password, and initial coin balance.
	 * The player is only inserted if they do not already exist in the database.
	 * </p>
	 */
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

	/**
	 * {@inheritDoc}
	 * <p>
	 * Deletes the player only if they exist in the database.
	 * </p>
	 */
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

	/**
	 * {@inheritDoc}
	 * <p>
	 * Updates the player's password. The player must already exist in the database.
	 * </p>
	 */
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

	/**
	 * {@inheritDoc}
	 * <p>
	 * Retrieves the player by their ID and eagerly loads their card collection
	 * via {@link #queryPlayerCards(int)}.
	 * </p>
	 */
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

	/**
	 * {@inheritDoc}
	 * <p>
	 * Queries the {@code PlayersCards} table to retrieve all card IDs and their
	 * quantities for the given player.
	 * </p>
	 */
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

	/**
	 * {@inheritDoc}
	 * <p>
	 * Searches the {@code Players} table by the {@code Username} column and
	 * eagerly loads the player's card collection.
	 * </p>
	 */
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

	/**
	 * Retrieves the quantity of a specific card owned by a player.
	 *
	 * @param player the {@link Player} to query
	 * @param card   the {@link Card} to check
	 * @return the quantity of the card owned by the player, or 0 if not owned
	 */
	public int queryCardQuantity(Player player, Card card) {
		int quantity = 0;
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLGETCARDQUANTITY);
			statement.setInt(1, player.getPlayerId());
			statement.setInt(2, card.getCardID());
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()) {
				quantity = resultado.getInt("Quantity");
			}
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return quantity;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * If the player already owns the card, increments the quantity in the
	 * {@code PlayersCards} table. Otherwise, inserts a new row with quantity 1.
	 * </p>
	 */
	@Override
	public boolean addCard(Player player, Card card) {
		boolean ok = false;
		HashMap<Integer, Integer> cards = queryPlayerCards(player.getPlayerId());
		int quantity = this.queryCardQuantity(player, card);
		this.openConnection();
		try {
			if (cards.containsKey(card.getCardID())) {
				statement = connection.prepareStatement(SQLUPDATECARDQUANTITY);
				statement.setInt(1, quantity + 1);
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
			e.printStackTrace();
		}

		return ok;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Deducts 500 coins from the player's balance if they have sufficient funds.
	 * </p>
	 */
	@Override
	public void buyPack(Player player) {
		// boolean ok = false;
		int coins = this.getGold(player);
		this.openConnection();
		if (coins >= 500) {
			try {
				statement = connection.prepareStatement(SQLUPDTEGOLD);
				statement.setInt(1, coins - 500);
				statement.setInt(2, player.getPlayerId());
				if (statement.executeUpdate() > 0) {
					// ok = true;
				}
				statement.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Queries the {@code Coins} column from the {@code Players} table for the
	 * specified player.
	 * </p>
	 */
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
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return coins;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Adds the specified amount of coins to the player's current balance.
	 * Negative values can be used to subtract coins.
	 * </p>
	 */
	@Override
	public void addCoins(Player player, int gold) {
		int coins = this.getGold(player);
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLUPDTEGOLD);
			statement.setInt(1, coins + gold);
			statement.setInt(2, player.getPlayerId());
			statement.executeUpdate();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Queries the {@code PackAvilable} column from the {@code Players} table
	 * for the specified player.
	 * </p>
	 */
	@Override
	public boolean checkFreePack(Player player) {
		boolean avilable = false;
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLCHECKFREEPACK);
			statement.setInt(1, player.getPlayerId());
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()) {
				avilable = resultado.getBoolean("PackAvilable");
			}
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return avilable;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Sets the {@code PackAvilable} column to 0 (false) in the {@code Players}
	 * table for the specified player.
	 * </p>
	 */
	@Override
	public boolean freePackOpend(Player player) {
		boolean ok = false;
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLUPDATEFREEPACK);
			statement.setInt(1, player.getPlayerId());
			if (statement.executeUpdate() > 0) {
				ok = true;
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ok;
	}

}