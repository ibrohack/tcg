package com.dami.tcg.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link ImplCardBD} class.
 * <p>
 * Uses Mockito to mock JDBC objects ({@link Connection},
 * {@link PreparedStatement},
 * {@link ResultSet}) so tests can run without a real database.
 * The class under test is instantiated via a package-private subclass that
 * overrides
 * the connection logic.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 */
@ExtendWith(MockitoExtension.class)
class ImplCardBDTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    private ImplCardBD implCardBD;

    // ======================== checkCard ========================

    @Test
    @DisplayName("checkCard should return true when card exists in DB")
    void checkCard_CardExists_ReturnsTrue() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);

            Card card = new Card(1, "Dragon", "Legendary", "A dragon", 100, 50);
            boolean result = implCardBD.checkCard(card);

            assertTrue(result);
            verify(mockResultSet).close();
            verify(mockStatement).close();
            verify(mockConnection).close();
        }
    }

    @Test
    @DisplayName("checkCard should return false when card does not exist in DB")
    void checkCard_CardNotExists_ReturnsFalse() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            Card card = new Card(99, "Ghost", "Mythic", "A ghost", 1000, 500);
            boolean result = implCardBD.checkCard(card);

            assertFalse(result);
        }
    }

    // ======================== insertCard ========================

    @Test
    @DisplayName("insertCard should return true when card is successfully inserted")
    void insertCard_NewCard_ReturnsTrue() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            // First call to checkCard: card does not exist
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);
            // Then insertCard tries insert
            when(mockStatement.executeUpdate()).thenReturn(1);

            Card card = new Card(0, "NewCard", "Common", "A new card", 10, 5);
            boolean result = implCardBD.insertCard(card);

            assertTrue(result);
        }
    }

    @Test
    @DisplayName("insertCard should return false when card already exists")
    void insertCard_ExistingCard_ReturnsFalse() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            // checkCard returns true - card exists
            when(mockResultSet.next()).thenReturn(true);

            Card card = new Card(1, "Dragon", "Legendary", "A dragon", 100, 50);
            boolean result = implCardBD.insertCard(card);

            assertFalse(result);
        }
    }

    // ======================== deleteCard ========================

    @Test
    @DisplayName("deleteCard should return true when card is successfully deleted")
    void deleteCard_ExistingCard_ReturnsTrue() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            // checkCard should return true - card exists
            when(mockResultSet.next()).thenReturn(true);
            when(mockStatement.executeUpdate()).thenReturn(1);

            Card card = new Card(1, "Dragon", "Legendary", "A dragon", 100, 50);
            boolean result = implCardBD.deleteCard(card);

            assertTrue(result);
        }
    }

    @Test
    @DisplayName("deleteCard should return false when card does not exist")
    void deleteCard_NonExistingCard_ReturnsFalse() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            // checkCard returns false - card does not exist
            when(mockResultSet.next()).thenReturn(false);

            Card card = new Card(99, "Ghost", "Mythic", "A ghost", 1000, 500);
            boolean result = implCardBD.deleteCard(card);

            assertFalse(result);
        }
    }

    // ======================== updateCard ========================

    @Test
    @DisplayName("updateCard should return true when card exists and update succeeds")
    void updateCard_ExistingCard_ReturnsTrue() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            // checkCard returns true
            when(mockResultSet.next()).thenReturn(true);
            when(mockStatement.executeUpdate()).thenReturn(1);

            Card card = new Card(1, "UpdatedDragon", "Legendary", "Updated description", 600, 300);
            boolean result = implCardBD.updateCard(card);

            assertTrue(result);
        }
    }

    @Test
    @DisplayName("updateCard should return false when card does not exist")
    void updateCard_NonExistingCard_ReturnsFalse() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            Card card = new Card(99, "Ghost", "Mythic", "A ghost", 1000, 500);
            boolean result = implCardBD.updateCard(card);

            assertFalse(result);
        }
    }

    // ======================== queryAll ========================

    @Test
    @DisplayName("queryAll should return a list of cards from the database")
    void queryAll_CardsExist_ReturnsList() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, true, false); // 2 rows

            when(mockResultSet.getInt("CardId")).thenReturn(1, 2);
            when(mockResultSet.getString("CardName")).thenReturn("Dragon", "Goblin");
            when(mockResultSet.getString("Quality")).thenReturn("Legendary", "Common");
            when(mockResultSet.getString("CardDescription")).thenReturn("A dragon", "A goblin");
            when(mockResultSet.getInt("PurchasePrice")).thenReturn(500, 50);
            when(mockResultSet.getInt("SellPrice")).thenReturn(250, 25);

            List<Card> result = implCardBD.queryAll();

            assertEquals(2, result.size());
            assertEquals("Dragon", result.get(0).getName());
            assertEquals("Goblin", result.get(1).getName());
        }
    }

    @Test
    @DisplayName("queryAll should return an empty list when no cards exist")
    void queryAll_NoCards_ReturnsEmptyList() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            List<Card> result = implCardBD.queryAll();

            assertTrue(result.isEmpty());
        }
    }

    // ======================== queryCard ========================

    @Test
    @DisplayName("queryCard should return a card when found by name")
    void queryCard_CardFound_ReturnsCard() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("CardId")).thenReturn(1);
            when(mockResultSet.getString("CardName")).thenReturn("Dragon");
            when(mockResultSet.getString("Quality")).thenReturn("Legendary");
            when(mockResultSet.getString("CardDescription")).thenReturn("A dragon");
            when(mockResultSet.getInt("PurchasePrice")).thenReturn(500);
            when(mockResultSet.getInt("SellPrice")).thenReturn(250);

            Card result = implCardBD.queryCard("Dragon");

            assertNotNull(result);
            assertEquals("Dragon", result.getName());
            assertEquals(1, result.getCardID());
        }
    }

    @Test
    @DisplayName("queryCard should return null when card not found")
    void queryCard_CardNotFound_ReturnsNull() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            Card result = implCardBD.queryCard("Nonexistent");

            assertNull(result);
        }
    }

    // ======================== queryCardId ========================

    @Test
    @DisplayName("queryCardId should return a card when found by ID")
    void queryCardId_CardFound_ReturnsCard() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("CardId")).thenReturn(5);
            when(mockResultSet.getString("CardName")).thenReturn("Phoenix");
            when(mockResultSet.getString("Quality")).thenReturn("Epic");
            when(mockResultSet.getString("CardDescription")).thenReturn("A mythical bird");
            when(mockResultSet.getInt("PurchasePrice")).thenReturn(300);
            when(mockResultSet.getInt("SellPrice")).thenReturn(150);

            Card result = implCardBD.queryCardId(5);

            assertNotNull(result);
            assertEquals(5, result.getCardID());
            assertEquals("Phoenix", result.getName());
        }
    }

    @Test
    @DisplayName("queryCardId should return null when card ID not found")
    void queryCardId_CardNotFound_ReturnsNull() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            Card result = implCardBD.queryCardId(999);

            assertNull(result);
        }
    }

    // ======================== queryRandomCard ========================

    @Test
    @DisplayName("queryRandomCard should return a card")
    void queryRandomCard_ReturnsCard() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("CardID")).thenReturn(3);
            when(mockResultSet.getString("CardName")).thenReturn("RandomCard");
            when(mockResultSet.getString("Quality")).thenReturn("Common");
            when(mockResultSet.getString("CardDescription")).thenReturn("A random card");
            when(mockResultSet.getInt("PurchasePrice")).thenReturn(20);
            when(mockResultSet.getInt("SellPrice")).thenReturn(10);

            Card result = implCardBD.queryRandomCard();

            assertNotNull(result);
            assertEquals("RandomCard", result.getName());
        }
    }

    // ======================== queryShopCards ========================

    @Test
    @DisplayName("queryShopCards with playerId 0 should return default cards")
    void queryShopCards_DefaultPlayer_ReturnsCards() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, false); // 1 row
            when(mockResultSet.getInt("CardID")).thenReturn(1);
            when(mockResultSet.getString("CardName")).thenReturn("ShopCard");
            when(mockResultSet.getString("Quality")).thenReturn("Rare");
            when(mockResultSet.getString("CardDescription")).thenReturn("A shop card");
            when(mockResultSet.getInt("PurchasePrice")).thenReturn(200);
            when(mockResultSet.getInt("SellPrice")).thenReturn(100);

            ArrayList<Card> result = implCardBD.queryShopCards(0);

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("ShopCard", result.get(0).getName());
        }
    }

    @Test
    @DisplayName("queryShopCards with specific playerId should query player-specific cards")
    void queryShopCards_SpecificPlayer_ReturnsPlayerCards() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, true, false); // 2 rows
            when(mockResultSet.getInt("CardID")).thenReturn(10, 20);
            when(mockResultSet.getString("CardName")).thenReturn("Card1", "Card2");
            when(mockResultSet.getString("Quality")).thenReturn("Common", "Rare");
            when(mockResultSet.getString("CardDescription")).thenReturn("Desc1", "Desc2");
            when(mockResultSet.getInt("PurchasePrice")).thenReturn(50, 200);
            when(mockResultSet.getInt("SellPrice")).thenReturn(25, 100);

            ArrayList<Card> result = implCardBD.queryShopCards(5);

            assertEquals(2, result.size());
            verify(mockStatement).setInt(1, 5);
        }
    }

    // ======================== purchaseShopCard ========================

    @Test
    @DisplayName("purchaseShopCard should return true on successful purchase")
    void purchaseShopCard_Success_ReturnsTrue() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeUpdate()).thenReturn(1);

            Card card = new Card(1, "Dragon", "Legendary", "A dragon", 500, 250);
            Player player = new Player(1, "testUser", "pass", 1000, null, null);

            boolean result = implCardBD.purchaseShopCard(card, player);

            assertTrue(result);
            verify(mockStatement).setBoolean(1, true);
            verify(mockStatement).setInt(2, 1); // card ID
            verify(mockStatement).setInt(3, 1); // player ID
        }
    }

    @Test
    @DisplayName("purchaseShopCard should return false when update affects no rows")
    void purchaseShopCard_NoRowsAffected_ReturnsFalse() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeUpdate()).thenReturn(0);

            Card card = new Card(99, "Ghost", "Mythic", "A ghost", 1000, 500);
            Player player = new Player(1, "testUser", "pass", 1000, null, null);

            boolean result = implCardBD.purchaseShopCard(card, player);

            assertFalse(result);
        }
    }

    // ======================== queryPurchasedCard ========================

    @Test
    @DisplayName("queryPurchasedCard should return true when card has been purchased")
    void queryPurchasedCard_Purchased_ReturnsTrue() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getBoolean("Purchased")).thenReturn(true);

            Card card = new Card(1, "Dragon", "Legendary", "A dragon", 500, 250);
            Player player = new Player(1, "testUser", "pass", 1000, null, null);

            boolean result = implCardBD.queryPurchasedCard(card, player);

            assertTrue(result);
        }
    }

    @Test
    @DisplayName("queryPurchasedCard should return false when card has not been purchased")
    void queryPurchasedCard_NotPurchased_ReturnsFalse() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getBoolean("Purchased")).thenReturn(false);

            Card card = new Card(1, "Dragon", "Legendary", "A dragon", 500, 250);
            Player player = new Player(1, "testUser", "pass", 1000, null, null);

            boolean result = implCardBD.queryPurchasedCard(card, player);

            assertFalse(result);
        }
    }

    @Test
    @DisplayName("queryPurchasedCard should return false when no shop entry exists")
    void queryPurchasedCard_NoEntry_ReturnsFalse() throws Exception {
        try (MockedStatic<DriverManager> driverMock = mockStatic(DriverManager.class)) {
            driverMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            implCardBD = new ImplCardBD();

            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false);

            Card card = new Card(99, "Ghost", "Mythic", "A ghost", 1000, 500);
            Player player = new Player(1, "testUser", "pass", 1000, null, null);

            boolean result = implCardBD.queryPurchasedCard(card, player);

            assertFalse(result);
        }
    }
}
