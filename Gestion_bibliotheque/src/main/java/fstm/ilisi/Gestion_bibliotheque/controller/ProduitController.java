package fstm.ilisi.Gestion_bibliotheque.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import fstm.ilisi.Gestion_bibliotheque.entity.Produit;
import fstm.ilisi.Gestion_bibliotheque.repository.ProduitRepository;

@Controller
public class ProduitController {
    
    //dipendance injection
    private final ProduitRepository produitRepository;
    
    // Chemin pour stocker les images uploadées
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    public ProduitController(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    @GetMapping("/index")
    public String index(Model model,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "5") int size,
      @RequestParam(name = "search", defaultValue = "") String search) {

        Page<Produit> produitsPage = produitRepository.findByNomContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCategorieContainsIgnoreCase(search, search, search, PageRequest.of(page, size));
        model.addAttribute("ListeProduit", produitsPage.getContent());
        model.addAttribute("pages", new int[produitsPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        return "ListeProduit";
    }

    @GetMapping("/deleteProduit")
    public String deleteProduit(@RequestParam(name = "id") Long id,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "search", defaultValue = "") String search) {
        produitRepository.deleteById(id);
        return "redirect:/index?page=" + page + "&search=" + search;
    }

    @GetMapping("/editProduit")
    public String showEditForm(@RequestParam(name = "id") Long id,
                               @RequestParam(name = "search", defaultValue = "") String search,
                               Model model) {
        Produit produit = produitRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Produit introuvable"));
        
         model.addAttribute("produit", produit);
         model.addAttribute("search", search);
        return "editProduit";
    }

    @PostMapping("/editProduit")
    public String saveProduit(@ModelAttribute Produit produit,
                            @RequestParam(name = "search", defaultValue = "") String search,
                            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile) {
        
        // Gérer l'upload de l'image si un fichier est fourni
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = saveImageFile(imageFile);
                produit.setImageUrl("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                // En cas d'erreur, on garde l'URL existante
            }
        }
        
        produitRepository.save(produit);
        return "redirect:/index?search=" + search;
    }

    @GetMapping("/addProduit")
    public String showAddForm(@RequestParam(name = "search", defaultValue = "") String search,
                            Model model) {
        model.addAttribute("produit", new Produit());
        model.addAttribute("search", search);
        return "ajouterProduit";
    }

    @PostMapping("/addProduit")
    public String addProduit(@ModelAttribute Produit produit,
                            @RequestParam(name = "search", defaultValue = "") String search,
                            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile) {
        
        // Gérer l'upload de l'image si un fichier est fourni
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = saveImageFile(imageFile);
                produit.setImageUrl("/uploads/" + fileName);
            } catch (IOException e) {
                e.printStackTrace();
                // En cas d'erreur, on utilise l'URL fournie dans le formulaire
            }
        }
        
        produitRepository.save(produit);
        return "redirect:/index?search=" + search;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/index";
    }
    
    // Méthode pour sauvegarder l'image uploadée
    private String saveImageFile(MultipartFile file) throws IOException {
        // Créer le dossier s'il n'existe pas
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Générer un nom de fichier unique
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;
        
        // Sauvegarder le fichier
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return fileName;
    }

}
