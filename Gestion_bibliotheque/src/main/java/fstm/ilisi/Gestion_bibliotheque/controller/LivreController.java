package fstm.ilisi.Gestion_bibliotheque.controller;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import fstm.ilisi.Gestion_bibliotheque.entity.Livre;
import fstm.ilisi.Gestion_bibliotheque.repository.LivreRepository;


@Controller
public class LivreController {

    private final LivreRepository livreRepository ;

    public LivreController(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }

    @GetMapping("/index")
    public String index(Model model ,
      @RequestParam(name = "page", defaultValue = "0") int page ,
      @RequestParam(name = "size", defaultValue = "5") int size,
      @RequestParam(name = "search", defaultValue = "") String search) {

        Page<Livre> livresPage = livreRepository.findByTitreContainsIgnoreCaseOrAuteurContainsIgnoreCase(search, search, PageRequest.of(page, size));
        model.addAttribute("ListeLivre", livresPage.getContent());
        model.addAttribute("pages", new int[livresPage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("search", search);
        return "ListeLivre";
    }

    @GetMapping("/deleteLivre")
public String deleteLivre(@RequestParam(name = "id") Long id,
                          @RequestParam(name = "page", defaultValue = "0") int page,
                          @RequestParam(name = "search", defaultValue = "") String search) {
    livreRepository.deleteById(id);
    return "redirect:/index?page=" + page + "&search=" + search;
}


    @GetMapping("/editLivre")
    public String showEditForm(@RequestParam(name = "id") Long id, 
                               @RequestParam(name = "search", defaultValue = "") String search,
                               Model model) {
        Livre livre = livreRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Livre introuvable"));
        
         model.addAttribute("livre", livre);
         model.addAttribute("search", search);
        return "editLivre";
    }

    @PostMapping("/editLivre")
    public String saveLivre(@ModelAttribute Livre livre,
                            @RequestParam(name = "search", defaultValue = "") String search) {
        livreRepository.save(livre);
        return "redirect:/index?search=" + search;
    }

    @GetMapping("/addLivre")
    public String showAddForm(@RequestParam(name = "search", defaultValue = "") String search,
                              Model model) {
        model.addAttribute("livre", new Livre());
        model.addAttribute("search", search);
        return "ajouterLivre";
    }

    @PostMapping("/addLivre")
    public String addLivre(@ModelAttribute Livre livre,
                            @RequestParam(name = "search", defaultValue = "") String search) {
        livreRepository.save(livre);
        return "redirect:/index?search=" + search;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/index";
    }

}
