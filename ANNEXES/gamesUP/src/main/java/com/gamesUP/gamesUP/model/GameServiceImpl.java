package com.gamesUP.gamesUP.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final RestTemplate restTemplate;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<Game> getAllGames() { 
        return gameRepository.findAll(); 
    }

    @Override
    public Game getGameById(Long id) {
        return gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Jeu non trouvé"));
    }

    @Override
    public List<Game> searchGames(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return gameRepository.findAll();
        return gameRepository.searchByTitle(keyword);
    }

    @Override
    public Game createGame(Game game) { 
        return gameRepository.save(game); 
    }

    @Override
    public Game updateGame(Long id, Game gameDetails) {
        Game game = getGameById(id);
        game.setTitle(gameDetails.getTitle());
        game.setDescription(gameDetails.getDescription());
        game.setPrice(gameDetails.getPrice());
        return gameRepository.save(game);
    }

    @Override
    public void deleteGame(Long id) { 
        gameRepository.delete(getGameById(id)); 
    }

    // --- LIAISON PYTHON FASTAPI CORRIGÉE ET SECURISÉE ---
    // --- LIAISON PYTHON FASTAPI DIRECTE ET SÉCURISÉE ---
    @Override
    public List<Long> getRecommendations(Long gameId) {
        String url = "http://127.0.0.1:8000/recommend";
        Game targetGame = getGameById(gameId);
        List<Game> allGames = gameRepository.findAll();

        // Extraction des listes de données simples
        List<Long> allIds = new ArrayList<>();
        List<Double> allPrices = new ArrayList<>();
        
        for (Game g : allGames) {
            allIds.add(g.getId());
            allPrices.add(g.getPrice());
        }

        // Construction du payload plat et conforme à FastAPI
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("target_price", targetGame.getPrice());
        requestBody.put("all_ids", allIds);
        requestBody.put("all_prices", allPrices);
        // Remplace requestBody.put("k", 3); par :
    requestBody.put("k", 1);

        try {
            // Envoi à FastAPI
            Map<String, Object> response = restTemplate.postForObject(url, requestBody, Map.class);
            
            List<Long> result = new ArrayList<>();
            if (response != null && response.get("recommended_game_ids") != null) {
                List<?> idsFromPython = (List<?>) response.get("recommended_game_ids");
                for (Object item : idsFromPython) {
                    if (item instanceof Number) {
                        result.add(((Number) item).longValue());
                    }
                }
            }
            return result;
        } catch (Exception e) {
            System.out.println(">> [ERREUR PASSERELLE IA] : " + e.getMessage());
            return new ArrayList<>();
        }
    }
}