package com.jae.playergames.event;

import com.jae.playergames.domain.Game;
import com.jae.playergames.domain.Player;

import java.io.Serializable;

public record MoveCreatedEvent(Game game, Player player) implements Serializable {

}
