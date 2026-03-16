package com.dami.tcg.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ImplDeckBD implements DeckDAO{

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
    final String SQLSELECT = "SELECT * FROM Cards WHERE Name = ?";
    final String SQLINSERT = "INSERT INTO Cards VALUES (?,?,?)";
    final String SQLCONSULTA = "SELECT * FROM Cards";
    final String SQLBORRAR = "DELETE FROM Cards WHERE CardId=?";
    final String SQLMODIFICAR = "UPDATE Cards SET Name=?, Quality=?, Description=? WHERE CardId=?";

    public ImplDeckBD() {
        this.configFile = ResourceBundle.getBundle("configClase");
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
            System.out.println("Error loading ");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error al intentar abrir la BD");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
	@Override
	public boolean checkCard(Deck deck) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertDeck(Deck deck) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteDeck(Deck deck) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateDeck(Deck deck) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Deck> queryAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Deck queryDeck(int deckId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
