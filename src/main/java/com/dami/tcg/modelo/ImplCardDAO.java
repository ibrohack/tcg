package com.dami.tcg.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ImplCardDAO implements CardDAO {
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

    public ImplCardDAO() {
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
                System.out.println("Error al insertar equipo: " + e.getMessage());
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
                System.out.println("Error al borrar equipo: " + e.getMessage());
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
                System.out.println("Error al modificar equipo: " + e.getMessage());
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
                cards.add(new Card(resultado.getInt("CardId"), resultado.getString("Name"),
                        resultado.getString("Quality"), resultado.getString("Description")));
            }
            resultado.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener equipos: " + e.getMessage());
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
                        resultado.getString("Name"),
                        resultado.getString("Quality"),
                        resultado.getString("Description"));
            }
            resultado.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener equipo por nombre: " + e.getMessage());
        }
        return card;
    }
}
