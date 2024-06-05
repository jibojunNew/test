package com.jae.playergames.event.consumer;

import com.jae.playergames.event.MoveCreatedEvent;
import com.jae.playergames.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MoveEventConsumer {

    private GameService gameService;

    @Value("${player.name}")
    private String playerName;

    public MoveEventConsumer(GameService gameService) {
        this.gameService = gameService;
    }

    @KafkaListener(topics = "gameTopic", groupId = "game-group")
    public void handleMoveCreatedEvent(MoveCreatedEvent event) {
        log.info("event received:{}", event);
        if (playerName.equals(event.player().getName())) {
            log.info("event ignored since it's sent by same player:{}", event);
            return;
        }

        var game = event.game();
        gameService.saveGame(game);
        if (!game.isFinished()) {
            gameService.makeMove(game.getId());
        } else {
            log.info("Game finished!");
        }
    }

}
