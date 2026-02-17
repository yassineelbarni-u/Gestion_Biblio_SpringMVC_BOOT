package fstm.ilisi.Gestion_bibliotheque.controller;

import fstm.ilisi.Gestion_bibliotheque.entity.Livre;
import fstm.ilisi.Gestion_bibliotheque.repository.LivreRepository;
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
 * Tests unitaires pour LivreController
 * Utilise JUnit 5 (Jupiter) et Mockito
 */


// active Mockito avec junit dans ce test
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du Contoller Livre")
class LivreControllerTest {
 
    // Mock des dépendances pour isoler le controller

    @Mock
    private LivreRepository livreRepository;

    @Mock
    private Model model;

    @InjectMocks
    private LivreController livreController;

    // MockMvc pour tester les endpoints HTTP
    private MockMvc mockMvc;
    private Livre livre1;
    private Livre livre2;

    // Initialisation avant chaque test
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(livreController).build();
        livre1 = new Livre(1L, "Java Programming", "John Doe", 5);
        livre2 = new Livre(2L, "Spring Boot Guide", "Jane Smith", 3);
    }


    @Test
    @DisplayName("Test index - Liste des livres")
    void testIndex() {

        // Arrange
        List<Livre> livres = Arrays.asList(livre1, livre2);
        Page<Livre> page = new PageImpl<>(livres);

        // simulation du comportement du repository pour retourner une page de livres
        when(livreRepository.findByTitreContainsIgnoreCaseOrAuteurContainsIgnoreCase(
                anyString(), anyString(), any(PageRequest.class))).thenReturn(page);

        // appel de la méthode index du controller
        String view = livreController.index(model, 0, 5, "");

        // verification du nom de la vue retournée
        assertEquals("ListeLivre", view);

        // verification que les attributs ont ete ajoutes au model
        verify(model).addAttribute("ListeLivre", livres);
        verify(model).addAttribute("currentPage", 0);
    }


    @Test
    @DisplayName("Test Supprimer Livre")
    void testDeleteLivre() {

        doNothing().when(livreRepository).deleteById(1L);

        
        String redirect = livreController.deleteLivre(1L, 0, "");

        // verification du redirection apres suppression

        assertEquals("redirect:/index?page=0&search=", redirect);
        verify(livreRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Test showEditForm")
    void testShowEditForm() {

        // Arrange
        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre1));

        // Act
        String view = livreController.showEditForm(1L, "", model);

        // Assert
        assertEquals("editLivre", view);
        verify(model).addAttribute("livre", livre1);
    }


    @Test
    @DisplayName("Test saveLivre")
    void testSaveLivre() {

        // Arrange
        when(livreRepository.save(any(Livre.class))).thenReturn(livre1);

        // Act
        String redirect = livreController.saveLivre(livre1, "");

        // Assert
        assertEquals("redirect:/index?search=", redirect);
        verify(livreRepository).save(livre1);
    }


    @Test
    @DisplayName("Test showAddForm")
    void testShowAddForm() {
        // Act
        String view = livreController.showAddForm("", model);

        // Assert
        assertEquals("ajouterLivre", view);

        verify(model).addAttribute(eq("livre"), any(Livre.class));
    }

    @Test
    @DisplayName("Test addLivre")
    void testAddLivre() {
        // Arrange
        when(livreRepository.save(any(Livre.class))).thenReturn(livre1);
        
        // Act
        String redirect = livreController.addLivre(livre1, "");

        // Assert
        assertEquals("redirect:/index?search=", redirect);
        verify(livreRepository).save(livre1);
    }

    @Test
    @DisplayName("Test home")
    void testHome() {
        String redirect = livreController.home();
        assertEquals("redirect:/index", redirect);
    }
        

    @Test
    @DisplayName("Test avec MockMvc ")
    void testIndexWithMockMvc() throws Exception {
        Page<Livre> page = new PageImpl<>(Arrays.asList(livre1));
        when(livreRepository.findByTitreContainsIgnoreCaseOrAuteurContainsIgnoreCase(
                anyString(), anyString(), any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(view().name("ListeLivre"));
    }
}
