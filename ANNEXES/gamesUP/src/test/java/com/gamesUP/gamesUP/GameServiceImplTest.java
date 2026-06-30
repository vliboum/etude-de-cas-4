package com.gamesUP.gamesUP;

import com.gamesUP.gamesUP.model.Game;
import com.gamesUP.gamesUP.model.GameRepository;
import com.gamesUP.gamesUP.model.GameServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    public void testGetAllGames() {
        Game game1 = new Game(1L, "Elden Ring", "RPG", 59.99, "Action");
        Game game2 = new Game(2L, "Cyberpunk", "RPG", 49.99, "Action");
        when(gameRepository.findAll()).thenReturn(Arrays.asList(game1, game2));

        List<Game> result = gameService.getAllGames();

        assertEquals(2, result.size());
        assertEquals("Elden Ring", result.get(0).getTitle());
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    public void testGetGameById_NotFound() {
        when(gameRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            gameService.getGameById(99L);
        });

        assertEquals("Jeu non trouvé", exception.getMessage());
    }
}