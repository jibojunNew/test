package com.jae.playergames.service;

import com.jae.playergames.domain.Game;
import com.jae.playergames.domain.Move;
import com.jae.playergames.domain.Player;

import java.util.UUID;

public interface GameService {

    Game createGame(int number, Player startingPlayer);

    Game getGame(UUID gameId);

    void saveGame(Game game);

    Move makeMove(UUID gameId);

}
