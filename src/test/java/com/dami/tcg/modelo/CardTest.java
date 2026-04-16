package com.dami.tcg.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Card} model class.
 * <p>
 * Verifies constructors, getters, setters, and the {@code toString()} method.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 */
class CardTest {

    private Card defaultCard;
    private Card paramCard;

    @BeforeEach
    void setUp() {
        defaultCard = new Card();
        paramCard = new Card(1, "Dragon", "Legendary", "A fearsome dragon", 500, 250);
    }

    // ======================== DEFAULT CONSTRUCTOR ========================

    @Test
    @DisplayName("Default constructor should initialise cardID to 0")
    void defaultConstructor_CardID_IsZero() {
        assertEquals(0, defaultCard.getCardID());
    }

    @Test
    @DisplayName("Default constructor should initialise name to empty string")
    void defaultConstructor_Name_IsEmpty() {
        assertEquals("", defaultCard.getName());
    }

    @Test
    @DisplayName("Default constructor should initialise quality to empty string")
    void defaultConstructor_Quality_IsEmpty() {
        assertEquals("", defaultCard.getQuality());
    }

    @Test
    @DisplayName("Default constructor should initialise description to empty string")
    void defaultConstructor_Description_IsEmpty() {
        assertEquals("", defaultCard.getDescription());
    }

    @Test
    @DisplayName("Default constructor should initialise purchasePrice to 0")
    void defaultConstructor_PurchasePrice_IsZero() {
        assertEquals(0, defaultCard.getPurchasePrice());
    }

    @Test
    @DisplayName("Default constructor should initialise sellPrice to 0")
    void defaultConstructor_SellPrice_IsZero() {
        assertEquals(0, defaultCard.getSellPrice());
    }

    // ======================== PARAMETERIZED CONSTRUCTOR ========================

    @Test
    @DisplayName("Parameterized constructor should set cardID correctly")
    void paramConstructor_CardID() {
        assertEquals(1, paramCard.getCardID());
    }

    @Test
    @DisplayName("Parameterized constructor should set name correctly")
    void paramConstructor_Name() {
        assertEquals("Dragon", paramCard.getName());
    }

    @Test
    @DisplayName("Parameterized constructor should set quality correctly")
    void paramConstructor_Quality() {
        assertEquals("Legendary", paramCard.getQuality());
    }

    @Test
    @DisplayName("Parameterized constructor should set description correctly")
    void paramConstructor_Description() {
        assertEquals("A fearsome dragon", paramCard.getDescription());
    }

    @Test
    @DisplayName("Parameterized constructor should set purchasePrice correctly")
    void paramConstructor_PurchasePrice() {
        assertEquals(500, paramCard.getPurchasePrice());
    }

    @Test
    @DisplayName("Parameterized constructor should set sellPrice correctly")
    void paramConstructor_SellPrice() {
        assertEquals(250, paramCard.getSellPrice());
    }

    // ======================== SETTERS ========================

    @Test
    @DisplayName("setCardID should update the card ID")
    void setCardID_UpdatesValue() {
        defaultCard.setCardID(42);
        assertEquals(42, defaultCard.getCardID());
    }

    @Test
    @DisplayName("setName should update the card name")
    void setName_UpdatesValue() {
        defaultCard.setName("Phoenix");
        assertEquals("Phoenix", defaultCard.getName());
    }

    @Test
    @DisplayName("setQuality should update the quality")
    void setQuality_UpdatesValue() {
        defaultCard.setQuality("Mythic");
        assertEquals("Mythic", defaultCard.getQuality());
    }

    @Test
    @DisplayName("setDescription should update the description")
    void setDescription_UpdatesValue() {
        defaultCard.setDescription("A mythical bird");
        assertEquals("A mythical bird", defaultCard.getDescription());
    }

    @Test
    @DisplayName("setPurchasePrice should update the purchase price")
    void setPurchasePrice_UpdatesValue() {
        defaultCard.setPurchasePrice(1000);
        assertEquals(1000, defaultCard.getPurchasePrice());
    }

    @Test
    @DisplayName("setSellPrice should update the sell price")
    void setSellPrice_UpdatesValue() {
        defaultCard.setSellPrice(300);
        assertEquals(300, defaultCard.getSellPrice());
    }

    // ======================== SETTERS ON PARAMETERIZED CARD ========================

    @Test
    @DisplayName("setName on parameterized card should overwrite the original name")
    void setName_OverwritesExistingValue() {
        paramCard.setName("Griffin");
        assertEquals("Griffin", paramCard.getName());
    }

    @Test
    @DisplayName("setQuality on parameterized card should overwrite the original quality")
    void setQuality_OverwritesExistingValue() {
        paramCard.setQuality("Common");
        assertEquals("Common", paramCard.getQuality());
    }

    // ======================== TOSTRING ========================

    @Test
    @DisplayName("toString should contain the card ID")
    void toString_ContainsCardID() {
        String result = paramCard.toString();
        assertTrue(result.contains("cardID=1"));
    }

    @Test
    @DisplayName("toString should contain the card name")
    void toString_ContainsName() {
        String result = paramCard.toString();
        assertTrue(result.contains("name=Dragon"));
    }

    @Test
    @DisplayName("toString should contain the quality")
    void toString_ContainsQuality() {
        String result = paramCard.toString();
        assertTrue(result.contains("quality=Legendary"));
    }

    @Test
    @DisplayName("toString should contain the description")
    void toString_ContainsDescription() {
        String result = paramCard.toString();
        assertTrue(result.contains("description=A fearsome dragon"));
    }

    @Test
    @DisplayName("toString should start with 'Card ['")
    void toString_StartsWithCardPrefix() {
        String result = paramCard.toString();
        assertTrue(result.startsWith("Card ["));
    }

    // ======================== EDGE CASES ========================

    @Test
    @DisplayName("setName with null should store null")
    void setName_NullValue() {
        defaultCard.setName(null);
        assertNull(defaultCard.getName());
    }

    @Test
    @DisplayName("setQuality with null should store null")
    void setQuality_NullValue() {
        defaultCard.setQuality(null);
        assertNull(defaultCard.getQuality());
    }

    @Test
    @DisplayName("setDescription with null should store null")
    void setDescription_NullValue() {
        defaultCard.setDescription(null);
        assertNull(defaultCard.getDescription());
    }

    @Test
    @DisplayName("Negative purchasePrice should be stored as-is")
    void setPurchasePrice_NegativeValue() {
        defaultCard.setPurchasePrice(-100);
        assertEquals(-100, defaultCard.getPurchasePrice());
    }

    @Test
    @DisplayName("Negative sellPrice should be stored as-is")
    void setSellPrice_NegativeValue() {
        defaultCard.setSellPrice(-50);
        assertEquals(-50, defaultCard.getSellPrice());
    }

    @Test
    @DisplayName("Large cardID values should be handled correctly")
    void setCardID_LargeValue() {
        defaultCard.setCardID(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, defaultCard.getCardID());
    }
}
