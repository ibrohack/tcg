package com.dami.tcg.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ImplPlayerBD implements PlayerDAO{
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
    final String SQLSELECT = "SELECT * FROM Player WHERE playerId = ?";
    final String SQLINSERT = "INSERT INTO Player VALUES (?,?,?)";
    final String SQLCONSULTA = "SELECT * FROM Player";
    final String SQLBORRAR = "DELETE FROM Player WHERE playerId=?";
    final String SQLMODIFICAR = "UPDATE Player SET password=? WHERE playerId=?";
    final String SQLSELECTCARDS = "SELECT * FROM HAS WHERE playerId=?";

    public ImplPlayerBD() {
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
            System.out.println("Error verifying card: " + e.getMessage());
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
                statement.setInt(1, player.getPlayerId());
                statement.setString(2, player.getUsername());
                statement.setString(3, player.getPasswore());
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
                System.out.println("Error deleting card: " + e.getMessage());
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
                statement.setString(1, player.getPasswore());
                statement.setInt(2, player.getPlayerId());
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
	public Player queryPlayer(int playerID) {
		Player player= null;
        this.openConnection();
        try {
        	statement = connection.prepareStatement(SQLSELECT);
            statement.setInt(1, player.getPlayerId());
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                player = new Player(
                        resultado.getInt("PlayerId"),
                        resultado.getString("Username"),
                        resultado.getString("Password"),
                        queryCardPlayer(playerID),
                        null);
            }
            resultado.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error getting card by name: " + e.getMessage());
        }
        return player;
	}

	@Override
	public HashMap<Integer, Integer> queryCardPlayer(int playerID) {
		HashMap<Integer,Integer> cards = new HashMap<Integer,Integer>();
		this.openConnection();
        try {
        	statement = connection.prepareStatement(SQLSELECTCARDS);
            statement.setInt(1, playerID);
            ResultSet resultado = statement.executeQuery();
            while(resultado.next()) {
            	cards.put(resultado.getInt("CardID"), resultado.getInt("quantity"));
            }
            resultado.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error getting card by name: " + e.getMessage());
        }
		return null;
	}
}