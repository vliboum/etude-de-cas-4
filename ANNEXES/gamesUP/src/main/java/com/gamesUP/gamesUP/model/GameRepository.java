package com.gamesUP.gamesUP.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    // Requête personnalisée pour le système de recherche 
    @Query("SELECT g FROM Game g WHERE LOWER(g.title) LIKE LOWER(concat('%', :keyword, '%'))")
    List<Game> searchByTitle(@Param("keyword") String keyword);
}