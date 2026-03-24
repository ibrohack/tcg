package com.dami.tcg.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ImplCardBD implements CardDAO {
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
	final String SQLSELECT = "SELECT * FROM Cards WHERE CardName = ?";
	final String SQLINSERT = "INSERT INTO Cards VALUES (?,?,?)";
	final String SQLCONSULTA = "SELECT * FROM Cards";
	final String SQLBORRAR = "DELETE FROM Cards WHERE CardId=?";
	final String SQLMODIFICAR = "UPDATE Cards SET CardName=?, Quality=?, CardDescription=? WHERE CardId=?";
	final String SQLRANDOM = "SELECT * FROM Cards WHERE Quality = ? ORDER BY RAND() LIMIT 1";


	public ImplCardBD() {
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

	@Override
	public List<Card> queryAll() {
		List<Card> cards = new ArrayList<>();
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLCONSULTA);
			ResultSet resultado = statement.executeQuery();
			while (resultado.next()) {
				cards.add(new Card(resultado.getInt("CardId"), resultado.getString("CardName"),
						resultado.getString("Quality"), resultado.getString("CardDescription")));
			}
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error getting cards: " + e.getMessage());
		}
		return cards;
	}

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
						resultado.getString("CardDescription"));
			}
			resultado.close();
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error getting card by name: " + e.getMessage());
		}
		return card;
	}

	@Override
	public Card queryRandomCard() {
		double r = Math.random(); 
		Card card = new Card();
		this.openConnection();
		try {
			statement = connection.prepareStatement(SQLRANDOM);
			if(r<0.009) {
				statement.setString(1, "Mythic");
			}else if(r<0.04) {
				statement.setString(1, "Legendary");
			}else if(r<0.1) {
				statement.setString(1, "Epic");
			}else if(r<0.25) {
				statement.setString(1, "Rare");
			}else {
				statement.setString(1, "Common");
			}
			ResultSet resultado = statement.executeQuery();
			if (resultado.next()) {
				card.setCardID(resultado.getInt("CardID"));
				card.setName(resultado.getString("CardName"));
				card.setDescription(resultado.getString("CardDescription"));
				card.setQuality(resultado.getString("Quality"));
			}
			statement.close();
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error getting card by name: " + e.getMessage());
		}	
		return card;
	}
}
