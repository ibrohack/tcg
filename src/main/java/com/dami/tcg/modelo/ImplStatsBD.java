package com.dami.tcg.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ImplStatsBD implements StatsDAO {

    private Connection connection;
    private PreparedStatement statement;
    private ResourceBundle configFile;
    private String driverBD;
    private String urlBD;
    private String userBD;
    private String passwordBD;

    // SQL statements
    final String SQLACTIVEPLAYERS = "SELECT COUNT(*) as count FROM Players";
    final String SQLMOSTCOMMONCARD = "SELECT c.*, SUM(pc.Quantity) as total " +
            "FROM Cards c JOIN PlayersCards pc ON c.CardId = pc.CardID " +
            "GROUP BY c.CardId ORDER BY total DESC LIMIT 1";
    final String SQLMOSTCOMMONCARDQUANTITY = "SELECT SUM(Quantity) as total " +
            "FROM PlayersCards GROUP BY CardID ORDER BY total DESC LIMIT 1";
    final String SQLLEASTFOUNDCARD = "SELECT c.*, SUM(pc.Quantity) as total " +
            "FROM Cards c JOIN PlayersCards pc ON c.CardId = pc.CardID " +
            "GROUP BY c.CardId ORDER BY total ASC LIMIT 1";
    final String SQLLEASTFOUNDCARDQUANTITY = "SELECT SUM(Quantity) as total " +
            "FROM PlayersCards GROUP BY CardID ORDER BY total ASC LIMIT 1";
    final String SQLUNCLAIMEDCARDS = "SELECT * " +
            "FROM Cards WHERE CardId NOT IN (SELECT CardID FROM PlayersCards)";
    final String SQLLATESTCARDS = "SELECT * FROM Cards ORDER BY CardId DESC LIMIT ?";

    public ImplStatsBD() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getActivePlayersCount() {
        int count = 0;
        this.openConnection();
        try {
            statement = connection.prepareStatement(SQLACTIVEPLAYERS);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                count = resultado.getInt("count");
            }
            resultado.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public Card getMostCommonCard() {
        Card card = null;
        this.openConnection();
        try {
            statement = connection.prepareStatement(SQLMOSTCOMMONCARD);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                card = new Card(resultado.getInt("CardId"), resultado.getString("CardName"),
                        resultado.getString("Quality"), resultado.getString("CardDescription"),
                        resultado.getInt("PurchasePrice"), resultado.getInt("SellPrice"));
            }
            resultado.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }

    @Override
    public int getMostCommonCardQuantity() {
        int qty = 0;
        this.openConnection();
        try {
            statement = connection.prepareStatement(SQLMOSTCOMMONCARDQUANTITY);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                qty = resultado.getInt("total");
            }
            resultado.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return qty;
    }

    @Override
    public Card getLeastFoundCard() {
        Card card = null;
        this.openConnection();
        try {
            statement = connection.prepareStatement(SQLLEASTFOUNDCARD);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                card = new Card(resultado.getInt("CardId"), resultado.getString("CardName"),
                        resultado.getString("Quality"), resultado.getString("CardDescription"),
                        resultado.getInt("PurchasePrice"), resultado.getInt("SellPrice"));
            }
            resultado.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }

    @Override
    public int getLeastFoundCardQuantity() {
        int qty = 0;
        this.openConnection();
        try {
            statement = connection.prepareStatement(SQLLEASTFOUNDCARDQUANTITY);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                qty = resultado.getInt("total");
            }
            resultado.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return qty;
    }

    @Override
    public List<Card> getUnclaimedCards() {
        List<Card> cards = new ArrayList<>();
        this.openConnection();
        try {
            statement = connection.prepareStatement(SQLUNCLAIMEDCARDS);
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
            e.printStackTrace();
        }
        return cards;
    }

    @Override
    public List<Card> getLatestCards(int limit) {
        List<Card> cards = new ArrayList<>();
        this.openConnection();
        try {
            statement = connection.prepareStatement(SQLLATESTCARDS);
            statement.setInt(1, limit);
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
            e.printStackTrace();
        }
        return cards;
    }
}
