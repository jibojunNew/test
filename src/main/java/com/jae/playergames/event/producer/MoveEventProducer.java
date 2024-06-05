package com.jae.playergames.event.producer;

import com.jae.playergames.domain.Game;
import com.jae.playergames.domain.Player;
import com.jae.playergames.event.MoveCreatedEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class MoveEventProducer {

    private KafkaTemplate<String, MoveCreatedEvent> kafkaTemplate;

    public void publishMoveCreatedEvent(Game game, Player player) {
        var event = new MoveCreatedEvent(game, player);
        kafkaTemplate.send("gameTopic", event);
        log.info("event sent:{}", event);
    }

}
