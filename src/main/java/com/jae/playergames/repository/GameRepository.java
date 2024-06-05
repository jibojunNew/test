package com.jae.playergames.repository;

import com.jae.playergames.domain.Game;

import java.util.UUID;

public interface GameRepository {

    void save(Game game);

    Game getById(UUID gameId);

    void deleteAll();
}
