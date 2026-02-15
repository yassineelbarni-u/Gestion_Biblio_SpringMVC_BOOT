package fstm.ilisi.Gestion_bibliotheque.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import fstm.ilisi.Gestion_bibliotheque.entity.Livre;


public interface LivreRepository extends JpaRepository<Livre, Long> {
    Page<Livre> findByTitreContainsIgnoreCaseOrAuteurContainsIgnoreCase(String titre, String auteur, PageRequest pageable);
    
}