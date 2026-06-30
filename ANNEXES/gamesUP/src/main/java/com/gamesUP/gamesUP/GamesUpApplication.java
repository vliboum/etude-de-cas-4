package com.gamesUP.gamesUP;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.GameRepository;

@SpringBootApplication
@EntityScan(basePackageClasses = {com.gamesUP.gamesUP.model.Game.class, com.gamesUP.gamesUP.model.User.class})
@EnableJpaRepositories(basePackages = "com.gamesUP.gamesUP.model")
public class GamesUpApplication {

    public static void main(String[] args) {
        SpringApplication.run(GamesUpApplication.class, args);
    }
    
    @Bean
    public CommandLineRunner dataLoader(GameRepository gameRepository) {
        return args -> {
            gameRepository.save(new Game(null, "Elden Ring", "Un chef-d'œuvre de FromSoftware", 59.99, "RPG"));
            gameRepository.save(new Game(null, "Cyberpunk 2077", "Un RPG futuriste dystopique", 49.99, "Action"));
            gameRepository.save(new Game(null, "The Witcher 3", "Un grand RPG en monde ouvert", 29.99, "RPG")); // <-- Troisième jeu ajouté !
            System.out.println(">> [DONNÉES DE TEST] Jeux injectés avec succès dans la base H2 !");
        };
    }
}