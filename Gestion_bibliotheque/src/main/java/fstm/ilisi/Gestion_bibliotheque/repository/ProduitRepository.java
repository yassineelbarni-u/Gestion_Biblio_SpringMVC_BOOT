package fstm.ilisi.Gestion_bibliotheque.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import fstm.ilisi.Gestion_bibliotheque.entity.Produit;

public interface ProduitRepository extends JpaRepository<Produit, Long> {
    Page<Produit> findByNomContainsIgnoreCaseOrDescriptionContainsIgnoreCaseOrCategorieContainsIgnoreCase(String nom, String description, String categorie, PageRequest pageable);
    
}
