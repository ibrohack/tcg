package com.dami.tcg.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class ImplDeckBD implements DeckDAO {

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
    final String SQLSELECT = "SELECT * FROM Decks WHERE DeckID = ?";
    final String SQLINSERT = "INSERT INTO Decks (DeckTitle, PlayerID, DeckDescription) VALUES (?,?,?)";
    final String SQLCONSULTA = "SELECT * FROM Decks";
    final String SQLBORRAR = "DELETE FROM Decks WHERE DeckId=?";
    final String SQLMODIFICAR = "UPDATE Decks SET Title=?, Description=? WHERE DeckId=?";
    final String SQLPLAYERCARD = "SELECT * FROM Cards WHERE CardId IN (SELECT CardId FROM PlayersCards WHERE PlayerId=?)";
    final String SQLPLAYERDECKS = "SELECT * FROM Decks WHERE PlayerId=?";

    public ImplDeckBD() {
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
    public boolean checkCard(Deck deck) {
        boolean existe = false;
        this.openConnection();
        try {
            statement = connection.prepareStatement(SQLSELECT);
            statement.setInt(1, deck.getDeckID());
            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                existe = true;
            }
            resultado.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error verifying deck: " + e.getMessage());
        }
        return existe;
    }

    @Override
    public boolean insertDeck(Deck deck) {
        boolean ok = false;
        if (!checkCard(deck)) {
            this.openConnection();
            try {
                statement = connection.prepareStatement(SQLINSERT, java.sql.Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, deck.getTitle());
                statement.setInt(2, deck.getPlayerID());
                statement.setString(3, deck.getDescription());
                if (statement.executeUpdate() > 0) {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        deck.setDeckID(generatedKeys.getInt(1));
                    }

                    if (deck.getCards() != null && !deck.getCards().isEmpty()) {
                        String insertCardSql = "INSERT INTO DecksCards (DeckID, CardID, Quantity) VALUES (?, ?, ?)";
                        try (PreparedStatement cardStmt = connection.prepareStatement(insertCardSql)) {
                            for (java.util.Map.Entry<Integer, Integer> entry : deck.getCards().entrySet()) {
                                cardStmt.setInt(1, deck.getDeckID());
                                cardStmt.setInt(2, entry.getKey());
                                cardStmt.setInt(3, entry.getValue());
                                cardStmt.addBatch();
                            }
                            cardStmt.executeBatch();
                        }
                    }
                    ok = true;
                }
                statement.close();
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error inserting deck: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return ok;
    }

    @Override
    public boolean deleteDeck(Deck deck) {
        boolean ok = false;
        if (checkCard(deck)) {
            this.openConnection();
            try {
                statement = connection.prepareStatement(SQLBORRAR);
                statement.setInt(1, deck.getDeckID());
                if (statement.executeUpdate() > 0) {
                    ok = true;
                }
                statement.close();
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error deleting deck: " + e.getMessage());
            }
        }
        return ok;
    }

    @Override
    public boolean updateDeck(Deck deck) {
        boolean ok = false;
        if (checkCard(deck)) {
            this.openConnection();
            try {
                statement = connection.prepareStatement(SQLMODIFICAR);
                statement.setString(1, deck.getTitle());
                statement.setString(2, deck.getDescription());
                statement.setInt(3, deck.getDeckID());
                if (statement.executeUpdate() > 0) {
                    ok = true;
                }
                statement.close();
                connection.close();
            } catch (SQLException e) {
                System.out.println("Error updating deck: " + e.getMessage());
            }
        }
        return ok;
    }

    private HashMap<Integer, Integer> getCardsForDeck(int deckId) {
        HashMap<Integer, Integer> cards = new HashMap<>();
        String sql = "SELECT CardID, Quantity FROM DecksCards WHERE DeckID = ?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, deckId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cards.put(rs.getInt("CardID"), rs.getInt("Quantity"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error getting cards for deck: " + e.getMessage());
        }
        return cards;
    }

    @Override
    public List<Deck> queryAll() {
        List<Deck> decks = new ArrayList<>();
        this.openConnection();
        try {
            statement = connection.prepareStatement(SQLCONSULTA);
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                decks.add(new Deck(resultado.getInt("DeckId"), resultado.getString("DeckTitle"),
                        resultado.getString("DeckDescription"), resultado.getInt("PlayerID"),
                        getCardsForDeck(resultado.getInt("DeckId"))));
            }
            resultado.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error getting decks: " + e.getMessage());
        }
        return decks;
    }

    @Override
    public Deck queryDeck(int deckId) {
        Deck deck = null;
        this.openConnection();
        try {
            statement = connection.prepareStatement(SQLSELECT);
            statement.setInt(1, deckId);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                deck = new Deck(
                        resultado.getInt("DeckId"),
                        resultado.getString("DeckTitle"),
                        resultado.getString("DeckDescription"),
                        resultado.getInt("PlayerID"),
                        getCardsForDeck(deckId));
            }
            resultado.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error getting deck by id: " + e.getMessage());
        }
        return deck;
    }

    @Override
    public List<Card> queryPlayerCards(int playerId) {
        List<Card> cards = new ArrayList<>();
        this.openConnection();
        try {
            statement = connection.prepareStatement(SQLPLAYERCARD);
            statement.setInt(1, playerId);
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                cards.add(new Card(
                        resultado.getInt("CardId"),
                        resultado.getString("CardName"),
                        resultado.getString("Quality"),
                        resultado.getString("CardDescription"),
                        resultado.getInt("PurchasePrice"),
                        resultado.getInt("SellPrice")));
            }
            resultado.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error getting player cards: " + e.getMessage());
        }
        return cards;
    }

    @Override
    public List<Deck> queryPlayerDecks(int playerId) {
        List<Deck> decks = new ArrayList<>();
        this.openConnection();
        try {
            statement = connection.prepareStatement(SQLPLAYERDECKS);
            statement.setInt(1, playerId);
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                decks.add(new Deck(
                        resultado.getInt("DeckId"),
                        resultado.getString("DeckTitle"),
                        resultado.getString("DeckDescription"),
                        playerId,
                        getCardsForDeck(resultado.getInt("DeckId"))));
            }
            resultado.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Error getting player decks: " + e.getMessage());
        }
        return decks;
    }
}