package com.gamesUP.gamesUP.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.GameService;

import io.micrometer.common.lang.NonNull;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*") // Permet à l'application Angular de communiquer sans blocage CORS
public class GameController {

    private final GameService gameService;

    // Injection par constructeur de notre interface de Service (Respect du DIP de SOLID)
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    // 1. Récupérer tous les jeux OU utiliser le système de recherche requis par le brief
    @GetMapping
    public ResponseEntity<List<Game>> getGames(@RequestParam(value = "search", required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            return ResponseEntity.ok(gameService.searchGames(search));
        }
        return ResponseEntity.ok(gameService.getAllGames());
    }

    // 2. Récupérer un jeu par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@NonNull @PathVariable Long id) {
        try {
            return ResponseEntity.ok(gameService.getGameById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 3. Ajouter un nouveau jeu (Protégé par rôle Admin par la suite)
    @PostMapping
    public ResponseEntity<Game> createGame(@RequestBody Game game) {
        Game createdGame = gameService.createGame(game);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGame);
    }

    // 4. Modifier un jeu existant (Protégé par rôle Admin par la suite)
    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody Game gameDetails) {
        try {
            return ResponseEntity.ok(gameService.updateGame(id, gameDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 5. Supprimer un jeu (Protégé par rôle Admin par la suite)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        try {
            gameService.deleteGame(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
@GetMapping("/{id}/recommendations")
    public ResponseEntity<List<Long>> getGameRecommendations(@PathVariable Long id) {
        return ResponseEntity.ok(gameService.getRecommendations(id));
    }

}