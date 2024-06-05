package com.jae.playergames.service;

import com.jae.playergames.domain.Game;
import com.jae.playergames.domain.Move;
import com.jae.playergames.domain.Player;
import com.jae.playergames.event.producer.MoveEventProducer;
import com.jae.playergames.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    private final MoveEventProducer moveEventProducer;

    @Value("${player.name}")
    private String playerName;

    @Override
    public Game createGame(int number, Player startingPlayer) {
        UUID gameId = UUID.randomUUID();
        Game game = new Game(gameId, number, false, startingPlayer, null);
        gameRepository.save(game);
        return game;
    }

    @Override
    public Game getGame(UUID gameId) {
        return gameRepository.getById(gameId);
    }

    @Override
    public void saveGame(Game game) {
        gameRepository.save(game);
    }

    @Override
    public Move makeMove(UUID gameId) {
        var game = getGame(gameId);
        if (game == null || game.isFinished()) {
            throw new IllegalStateException("Game not started or already finished.");
        }
        Move move = game.makeMove();
        saveGame(game);
        moveEventProducer.publishMoveCreatedEvent(game, new Player(playerName));
        return move;
    }
}
