package com.dami.tcg.controller;

import com.dami.tcg.modelo.Card;
import com.dami.tcg.modelo.CardDAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link CardController} class.
 * <p>
 * Uses Mockito to mock the {@link CardDAO} dependency so that tests do not
 * require a live database connection.
 * </p>
 *
 * @author Brayan, Adam, Oihan and Asier
 */
@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    @Mock
    private CardDAO dao;

    @InjectMocks
    private CardController controller;

    private Card sampleCard;
    private Card anotherCard;

    @BeforeEach
    void setUp() {
        sampleCard = new Card(1, "Dragon", "Legendary", "A fearsome dragon", 500, 250);
        anotherCard = new Card(2, "Goblin", "Common", "A sneaky goblin", 50, 25);
    }

    // ======================== checkCard ========================

    @Test
    @DisplayName("checkCard should return true when the card exists")
    void checkCard_CardExists_ReturnsTrue() {
        when(dao.checkCard(sampleCard)).thenReturn(true);

        assertTrue(controller.checkCard(sampleCard));
        verify(dao, times(1)).checkCard(sampleCard);
    }

    @Test
    @DisplayName("checkCard should return false when the card does not exist")
    void checkCard_CardDoesNotExist_ReturnsFalse() {
        when(dao.checkCard(sampleCard)).thenReturn(false);

        assertFalse(controller.checkCard(sampleCard));
        verify(dao, times(1)).checkCard(sampleCard);
    }

    // ======================== insertCard ========================

    @Test
    @DisplayName("insertCard should return true on successful insertion")
    void insertCard_Success_ReturnsTrue() {
        when(dao.insertCard(sampleCard)).thenReturn(true);

        assertTrue(controller.insertCard(sampleCard));
        verify(dao, times(1)).insertCard(sampleCard);
    }

    @Test
    @DisplayName("insertCard should return false on failed insertion")
    void insertCard_Failure_ReturnsFalse() {
        when(dao.insertCard(sampleCard)).thenReturn(false);

        assertFalse(controller.insertCard(sampleCard));
        verify(dao, times(1)).insertCard(sampleCard);
    }

    // ======================== deleteCard ========================

    @Test
    @DisplayName("deleteCard should return true on successful deletion")
    void deleteCard_Success_ReturnsTrue() {
        when(dao.deleteCard(sampleCard)).thenReturn(true);

        assertTrue(controller.deleteCard(sampleCard));
        verify(dao, times(1)).deleteCard(sampleCard);
    }

    @Test
    @DisplayName("deleteCard should return false on failed deletion")
    void deleteCard_Failure_ReturnsFalse() {
        when(dao.deleteCard(sampleCard)).thenReturn(false);

        assertFalse(controller.deleteCard(sampleCard));
        verify(dao, times(1)).deleteCard(sampleCard);
    }

    // ======================== updateCard ========================

    @Test
    @DisplayName("updateCard should return true on successful update")
    void updateCard_Success_ReturnsTrue() {
        when(dao.updateCard(sampleCard)).thenReturn(true);

        assertTrue(controller.updateCard(sampleCard));
        verify(dao, times(1)).updateCard(sampleCard);
    }

    @Test
    @DisplayName("updateCard should return false on failed update")
    void updateCard_Failure_ReturnsFalse() {
        when(dao.updateCard(sampleCard)).thenReturn(false);

        assertFalse(controller.updateCard(sampleCard));
        verify(dao, times(1)).updateCard(sampleCard);
    }

    // ======================== queryAll ========================

    @Test
    @DisplayName("queryAll should return a list of cards")
    void queryAll_ReturnsCardList() {
        List<Card> expectedCards = Arrays.asList(sampleCard, anotherCard);
        when(dao.queryAll()).thenReturn(expectedCards);

        List<Card> result = controller.queryAll();

        assertEquals(2, result.size());
        assertEquals(expectedCards, result);
        verify(dao, times(1)).queryAll();
    }

    @Test
    @DisplayName("queryAll should return an empty list when no cards exist")
    void queryAll_NoCards_ReturnsEmptyList() {
        when(dao.queryAll()).thenReturn(Collections.emptyList());

        List<Card> result = controller.queryAll();

        assertTrue(result.isEmpty());
        verify(dao, times(1)).queryAll();
    }

    // ======================== queryCard ========================

    @Test
    @DisplayName("queryCard should return a card when found by name")
    void queryCard_CardFound_ReturnsCard() {
        when(dao.queryCard("Dragon")).thenReturn(sampleCard);

        Card result = controller.queryCard("Dragon");

        assertNotNull(result);
        assertEquals("Dragon", result.getName());
        assertEquals("Legendary", result.getQuality());
        verify(dao, times(1)).queryCard("Dragon");
    }

    @Test
    @DisplayName("queryCard should return null when no card is found")
    void queryCard_CardNotFound_ReturnsNull() {
        when(dao.queryCard("Nonexistent")).thenReturn(null);

        Card result = controller.queryCard("Nonexistent");

        assertNull(result);
        verify(dao, times(1)).queryCard("Nonexistent");
    }

    // ======================== listCards (Spring MVC endpoint) ========================

    @Test
    @DisplayName("listCards should return the 'cards' view name")
    void listCards_ReturnsCardsViewName() {
        when(dao.queryAll()).thenReturn(Collections.emptyList());
        Model model = new ConcurrentModel();

        String viewName = controller.listCards(model);

        assertEquals("cards", viewName);
    }

    @Test
    @DisplayName("listCards should add cards attribute to the model")
    void listCards_AddsCardsToModel() {
        List<Card> expectedCards = Arrays.asList(sampleCard, anotherCard);
        when(dao.queryAll()).thenReturn(expectedCards);
        Model model = new ConcurrentModel();

        controller.listCards(model);

        assertTrue(model.containsAttribute("cards"));
        @SuppressWarnings("unchecked")
        List<Card> modelCards = (List<Card>) model.getAttribute("cards");
        assertEquals(2, modelCards.size());
    }

    @Test
    @DisplayName("listCards should add an empty list to model when no cards exist")
    void listCards_NoCards_AddsEmptyListToModel() {
        when(dao.queryAll()).thenReturn(Collections.emptyList());
        Model model = new ConcurrentModel();

        controller.listCards(model);

        assertTrue(model.containsAttribute("cards"));
        @SuppressWarnings("unchecked")
        List<Card> modelCards = (List<Card>) model.getAttribute("cards");
        assertTrue(modelCards.isEmpty());
    }

    @Test
    @DisplayName("listCards should call dao.queryAll exactly once")
    void listCards_CallsDaoQueryAllOnce() {
        when(dao.queryAll()).thenReturn(Collections.emptyList());
        Model model = new ConcurrentModel();

        controller.listCards(model);

        verify(dao, times(1)).queryAll();
    }

    // ======================== DELEGATION VERIFICATION ========================

    @Test
    @DisplayName("All controller methods should delegate to the DAO")
    void allMethods_DelegateToDAO() {
        when(dao.checkCard(sampleCard)).thenReturn(true);
        when(dao.insertCard(sampleCard)).thenReturn(true);
        when(dao.deleteCard(sampleCard)).thenReturn(true);
        when(dao.updateCard(sampleCard)).thenReturn(true);
        when(dao.queryAll()).thenReturn(Collections.emptyList());
        when(dao.queryCard("Dragon")).thenReturn(sampleCard);

        controller.checkCard(sampleCard);
        controller.insertCard(sampleCard);
        controller.deleteCard(sampleCard);
        controller.updateCard(sampleCard);
        controller.queryAll();
        controller.queryCard("Dragon");

        verify(dao).checkCard(sampleCard);
        verify(dao).insertCard(sampleCard);
        verify(dao).deleteCard(sampleCard);
        verify(dao).updateCard(sampleCard);
        verify(dao).queryAll();
        verify(dao).queryCard("Dragon");
    }
}
