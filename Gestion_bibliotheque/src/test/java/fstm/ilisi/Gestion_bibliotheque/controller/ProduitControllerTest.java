package fstm.ilisi.Gestion_bibliotheque.controller;

import fstm.ilisi.Gestion_bibliotheque.entity.Produit;
import fstm.ilisi.Gestion_bibliotheque.repository.ProduitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitaires pour ProduitController
 * Utilise JUnit 5 (Jupiter) et Mockito
 */

// active Mockito avec junit dans ce test
@ExtendWith(MockitoExtension.class)

@DisplayName("Tests du Controller Produit")
class ProduitControllerTest {

    // Mock des dépendances pour isoler le controller
    @Mock
    private ProduitRepository produitRepository;
    
    // Mock du model pour verifier les attributs ajoutes
    @Mock
    private Model model;

    @InjectMocks
    private ProduitController produitController;

    // MockMvc pour tester les endpoints HTTP
    private MockMvc mockMvc;
    private Produit produit1;
    private Produit produit2;

    // Initialisation avant chaque test
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(produitController).build();
        produit1 = new Produit(1L, "Smartphone", "Electronique", "https://example.com/smartphone.jpg", "Téléphone haut de gamme", 599.99, 10);
        produit2 = new Produit(2L, "Ordinateur portable", "Informatique", "https://example.com/laptop.jpg", "PC portable 15 pouces", 899.99, 5);
    }

    @Test
    @DisplayName("Test index - Liste des produits")
    void testIndex() {

        // Arrange
        List<Produit> produits = Arrays.asList(produit1, produit2);
        Page<Produit> page = new PageImpl<>(produits);

        // simulation du comportement du repository pour retourner une page de produits
        when(produitRepository.findByNomContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCategorieContainsIgnoreCase(
                anyString(), anyString(), anyString(), any(PageRequest.class))).thenReturn(page);

        // appel de la méthode index du controller
        String view = produitController.index(model, 0, 5, "");

        // verification du nom de la vue retournée
        assertEquals("ListeProduit", view);

        // verification que les attributs ont ete ajoutes au model
        verify(model).addAttribute("ListeProduit", produits);
        verify(model).addAttribute("currentPage", 0);
    }

    @Test
    @DisplayName("Test Supprimer Produit")
    void testDeleteProduit() {

        // Arrange (pour simuler la suppression d'un produit)
        doNothing().when(produitRepository).deleteById(1L);
        
        String redirect = produitController.deleteProduit(1L, 0, "");

        assertEquals("redirect:/index?page=0&search=", redirect);
        verify(produitRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Test showEditForm")
    void testShowEditForm() {

        // Arrange
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit1));

        // Act
        String view = produitController.showEditForm(1L, "", model);

        // Assert
        assertEquals("editProduit", view);
        verify(model).addAttribute("produit", produit1);
    }

    @Test
    @DisplayName("Test saveProduit")
    void testSaveProduit() {

        // Arrange
        when(produitRepository.save(any(Produit.class))).thenReturn(produit1);

        // Act
        String redirect = produitController.saveProduit(produit1, "", null);

        // Assert
        assertEquals("redirect:/index?search=", redirect);
        verify(produitRepository).save(produit1);
    }

    @Test
    @DisplayName("Test showAddForm")
    void testShowAddForm() {
        // Act
        String view = produitController.showAddForm("", model);

        // Assert
        assertEquals("ajouterProduit", view);

        verify(model).addAttribute(eq("produit"), any(Produit.class));
    }

    @Test
    @DisplayName("Test addProduit")
    void testAddProduit() {
        // Arrange
        when(produitRepository.save(any(Produit.class))).thenReturn(produit1);

        // Act
        String redirect = produitController.addProduit(produit1, "", null);

        // Assert
        assertEquals("redirect:/index?search=", redirect);
        verify(produitRepository).save(produit1);
    }

    @Test
    @DisplayName("Test home")
    void testHome() {
        String redirect = produitController.home();
        assertEquals("redirect:/index", redirect);
    }

    @Test
    @DisplayName("Test avec MockMvc ")
    void testIndexWithMockMvc() throws Exception {
        Page<Produit> page = new PageImpl<>(Arrays.asList(produit1));
        when(produitRepository.findByNomContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCategorieContainsIgnoreCase(
                anyString(), anyString(), anyString(), any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("ListeProduit"));
    }
}
