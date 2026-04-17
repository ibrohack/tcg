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
 * JDBC-based implementation of the {@link StatsDAO} interface.
 * <p>
 * Provides concrete database operations for retrieving aggregate game statistics
 * displayed on the home page dashboard. Queries include player counts, card
 * popularity metrics, unclaimed cards, and latest card releases. Database connection
 * parameters are loaded from the {@code configDB.properties} resource bundle.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 * @see StatsDAO
 */
public class ImplStatsBD implements StatsDAO {

    /** The active JDBC connection to the database. */
    private Connection connection;

    /** The prepared statement used for executing SQL queries. */
    private PreparedStatement statement;

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
    final String SQLRARESTCARD = "SELECT * FROM Cards WHERE CardId = rarestCard()";

    /**
     * Constructs a new {@code ImplStatsBD} instance and loads database configuration
     * from the {@code configDB.properties} resource bundle.
     */
    public ImplStatsBD() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Counts the total number of rows in the {@code Players} table.
     * </p>
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Joins the {@code Cards} and {@code PlayersCards} tables, groups by card,
     * and selects the card with the highest total quantity across all players.
     * </p>
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Returns the sum of quantities for the most commonly owned card across all players.
     * </p>
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Joins the {@code Cards} and {@code PlayersCards} tables, groups by card,
     * and selects the card with the lowest total quantity across all players.
     * </p>
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Returns the sum of quantities for the least commonly owned card across all players.
     * </p>
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Selects all cards from the {@code Cards} table whose IDs do not appear
     * in the {@code PlayersCards} table (i.e., no player owns them).
     * </p>
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Queries the {@code Cards} table ordered by {@code CardId} descending,
     * limited to the specified number of results.
     * </p>
     */
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

    /**
     * {@inheritDoc}
     * <p>
     * Selects the card using the SQL function {@code rarestCard()} which returns
     * the ID of the card with the lowest overall quantity.
     * </p>
     */
    @Override
    public Card getRarestCard() {
        Card card = null;
        this.openConnection();
        try {
            statement = connection.prepareStatement(SQLRARESTCARD);
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
}
