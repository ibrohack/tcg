package com.dami.tcg.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * JDBC-based implementation of the {@link CardDAO} interface.
 * <p>
 * Provides concrete database operations for {@link Card} entities using
 * direct JDBC connections. Database connection parameters are loaded from
 * the {@code configDB.properties} resource bundle.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 * @see CardDAO
 */
public class ImplCardBD implements CardDAO {
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
	final String SQLSELECT = "SELECT * FROM Cards WHERE CardName = ?";
	final String SQLSELECTID = "SELECT * FROM Cards WHERE CardId = ?";
	final String SQLINSERT = "INSERT INTO Cards VALUES (?,?,?)";
	final String SQLCONSULTA = "SELECT * FROM Cards";
	final String SQLBORRAR = "DELETE FROM Cards WHERE CardId=?";
	final String SQLMODIFICAR = "UPDATE Cards SET CardName=?, Quality=?, CardDescription=? WHERE CardId=?";
	final String SQLRANDOM = "SELECT * FROM Cards WHERE Quality = ? ORDER BY RAND() LIMIT 1";
	final String SQLQUERYSHOPCARDS = "SELECT * FROM Cards C JOIN ShopCards S ON C.CardID=S.CardID WHERE S.PlayerID = ?";
	final String SQLUPDATESHOPSTATUS = "UPDATE ShopCards SET Purchased=? WHERE CardID=? AND PlayerID=?";
	final String SQLQUERYDEFAULTSHOPCARDS = "SELECT * FROM Cards ORDER BY RAND() LIMIT 3";
	final String SQLSELECTCARDSHOPID = "SELECT Purchased FROM ShopCards WHERE CardId = ? AND PlayerID=?";

	/**
	 * Constructs a new {@code ImplCardBD} instance and loads database configuration
	 * from the {@code configDB.properties} resource bundle.
	 */
	public ImplCardBD() {
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
	 * Checks for the existence of the card by querying the database using the card's ID.
	 * </p>
	 */
	@Override
	public boolean checkCard(Card card) {
		boolean existe = false;
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLSELECT);
			statement.setInt(1, card.getCardID());
			ResultSet resultado = statement.executeQuery();

			if (resultado.next()) {
				existe = true;
			}
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error verifying card: " + e.getMessage());
		}
		return existe;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Inserts the card only if it does not already exist in the database.
	 * </p>
	 */
	@Override
	public boolean insertCard(Card card) {
		boolean ok = false;
		if (!checkCard(card)) {
			this.openConnection();
			try {
				statement = connection.prepareStatement(SQLINSERT);
				statement.setString(1, card.getName());
				statement.setString(2, card.getQuality());
				statement.setString(3, card.getDescription());
				if (statement.executeUpdate() > 0) {
					ok = true;
				}
				statement.close();
				connection.close();
			} catch (SQLException e) {
				System.out.println("Error inserting card: " + e.getMessage());
			}
		}
		return ok;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Deletes the card only if it exists in the database.
	 * </p>
	 */
	@Override
	public boolean deleteCard(Card card) {
		boolean ok = false;
		if (checkCard(card)) {
			this.openConnection();
			try {
				statement = connection.prepareStatement(SQLBORRAR);
				statement.setInt(1, card.getCardID());
				if (statement.executeUpdate() > 0) {
					ok = true;
				}
				statement.close();
				connection.close();
			} catch (SQLException e) {
				System.out.println("Error deleting card: " + e.getMessage());
			}
		}
		return ok;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Updates the card's name, quality, and description. The card must already exist
	 * in the database.
	 * </p>
	 */
	@Override
	public boolean updateCard(Card card) {
		boolean ok = false;
		if (checkCard(card)) {
			this.openConnection();
			try {
				statement = connection.prepareStatement(SQLMODIFICAR);
				statement.setString(1, card.getName());
				statement.setString(2, card.getQuality());
				statement.setString(3, card.getDescription());
				statement.setInt(4, card.getCardID());
				if (statement.executeUpdate() > 0) {
					ok = true;
				}
				statement.close();
				connection.close();
			} catch (SQLException e) {
				System.out.println("Error updating card: " + e.getMessage());
			}
		}
		return ok;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Retrieves all cards from the {@code Cards} table, including their purchase
	 * and sell prices.
	 * </p>
	 */
	@Override
	public List<Card> queryAll() {
		List<Card> cards = new ArrayList<>();
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLCONSULTA);
			ResultSet resultado = statement.executeQuery();
			while (resultado.next()) {
				cards.add(new Card(resultado.getInt("CardId"), resultado.getString("CardName"),
						resultado.getString("Quality"), resultado.getString("CardDescription"),
						resultado.getInt("PurchasePrice"), resultado.getInt("SellPrice")));
			}
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error getting cards: " + e.getMessage());
		}
		return cards;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Searches for a card by its {@code CardName} column value.
	 * </p>
	 */
	@Override
	public Card queryCard(String name) {
		Card card = null;
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLSELECT);
			statement.setString(1, name);
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()) {
				card = new Card(
						resultado.getInt("CardId"),
						resultado.getString("CardName"),
						resultado.getString("Quality"),
						resultado.getString("CardDescription"),
						resultado.getInt("PurchasePrice"),
						resultado.getInt("SellPrice"));
			}
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error getting card by name: " + e.getMessage());
		}
		return card;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Searches for a card by its {@code CardId} column value.
	 * </p>
	 */
	@Override
	public Card queryCardId(int cardId) {
		Card card = null;
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLSELECTID);
			statement.setInt(1, cardId);
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()) {
				card = new Card(
						resultado.getInt("CardId"),
						resultado.getString("CardName"),
						resultado.getString("Quality"),
						resultado.getString("CardDescription"),
						resultado.getInt("PurchasePrice"),
						resultado.getInt("SellPrice"));
			}
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error getting card by id: " + e.getMessage());
		}
		return card;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Selects a random card from the database based on rarity probabilities:
	 * <ul>
	 *   <li>Mythic: 1% chance</li>
	 *   <li>Legendary: 4% chance</li>
	 *   <li>Epic: 10% chance</li>
	 *   <li>Rare: 25% chance</li>
	 *   <li>Common: 60% chance</li>
	 * </ul>
	 * </p>
	 */
	@Override
	public Card queryRandomCard() {
		double r = Math.random();
		Card card = new Card();
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLRANDOM);
			if (r < 0.01) {
				statement.setString(1, "Mythic");
			} else if (r < 0.05) {
				statement.setString(1, "Legendary");
			} else if (r < 0.15) {
				statement.setString(1, "Epic");
			} else if (r < 0.4) {
				statement.setString(1, "Rare");
			} else {
				statement.setString(1, "Common");
			}
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()) {
				card.setCardID(resultado.getInt("CardID"));
				card.setName(resultado.getString("CardName"));
				card.setDescription(resultado.getString("CardDescription"));
				card.setQuality(resultado.getString("Quality"));
				card.setPurchasePrice(resultado.getInt("PurchasePrice"));
				card.setSellPrice(resultado.getInt("SellPrice"));
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error getting card by name: " + e.getMessage());
		}
		return card;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * If {@code playerId} is 0, returns 3 random cards as a default shop display
	 * for unauthenticated users. Otherwise, queries the {@code ShopCards} table
	 * for the player's assigned shop cards.
	 * </p>
	 */
	@Override
	public ArrayList<Card> queryShopCards(int playerId) {
		ArrayList<Card> cards = new ArrayList<Card>();
		this.openConnection();
		try {
			if (playerId == 0) {
				statement = connection.prepareStatement(SQLQUERYDEFAULTSHOPCARDS);
			} else {
				statement = connection.prepareStatement(SQLQUERYSHOPCARDS);
				statement.setInt(1, playerId);
			}
			ResultSet resultado = statement.executeQuery();
			while (resultado.next()) {
				Card card = new Card();
				card.setCardID(resultado.getInt("CardID"));
				card.setName(resultado.getString("CardName"));
				card.setDescription(resultado.getString("CardDescription"));
				card.setQuality(resultado.getString("Quality"));
				card.setPurchasePrice(resultado.getInt("PurchasePrice"));
				card.setSellPrice(resultado.getInt("SellPrice"));
				cards.add(card);
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error getting card by name: " + e.getMessage());
		}
		return cards;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Sets the {@code Purchased} flag to {@code true} in the {@code ShopCards} table
	 * for the given card and player combination.
	 * </p>
	 */
	@Override
	public boolean purchaseShopCard(Card card, Player player) {
		boolean purchased=false;
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLUPDATESHOPSTATUS);
			statement.setBoolean(1, true);
			statement.setInt(2, card.getCardID());
			statement.setInt(3, player.getPlayerId());
			if (statement.executeUpdate() > 0) {
				purchased = true;
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error updating card: " + e.getMessage());
		}
		return purchased;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Queries the {@code ShopCards} table to determine if the specified card
	 * has been purchased by the given player.
	 * </p>
	 */
	@Override
	public boolean queryPurchasedCard(Card card, Player player) {
		boolean purchased=false;
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLSELECTCARDSHOPID);
			statement.setInt(1, card.getCardID());
			statement.setInt(2, player.getPlayerId());
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()) {
				purchased = resultado.getBoolean("Purchased");
			}
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error updating card: " + e.getMessage());
		}
		return purchased;
	}
}
