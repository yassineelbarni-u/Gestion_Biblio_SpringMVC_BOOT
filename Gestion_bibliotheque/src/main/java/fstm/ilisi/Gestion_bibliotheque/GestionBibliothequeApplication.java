package fstm.ilisi.Gestion_bibliotheque;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fstm.ilisi.Gestion_bibliotheque.repository.LivreRepository;

@SpringBootApplication
public class GestionBibliothequeApplication  {

	  @Autowired
      private LivreRepository livreRepository;

	public static void main(String[] args) {
		SpringApplication.run(GestionBibliothequeApplication.class, args);
	}

	// @Override
	// public void run(String... args) throws Exception {
		
	// 	livreRepository.save(new Livre(null, "Design patterns", "Erich Gamma", 5));
	// 	livreRepository.save(new Livre(null, "Java", "Yassine", 3));
	// 	livreRepository.save(new Livre(null, "test 2", "yassine", 2));
	// 	livreRepository.save(new Livre(null, "test 3", "ayyoub", 4));
	
	// }

}
