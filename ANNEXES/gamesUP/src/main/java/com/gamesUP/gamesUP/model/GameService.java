package com.gamesUP.gamesUP.model;

import java.util.List;

public interface GameService {
    List<Game> getAllGames();
    Game getGameById(Long id);
    List<Game> searchGames(String keyword);
    Game createGame(Game game);
    Game updateGame(Long id, Game game);
    void deleteGame(Long id);

    List<Long> getRecommendations(Long gameId);
}