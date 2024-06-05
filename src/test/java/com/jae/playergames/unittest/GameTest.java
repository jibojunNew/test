package com.jae.playergames.unittest;

import com.jae.playergames.domain.Game;
import com.jae.playergames.domain.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

class GameTest {

    @Test
    void testMakeMoveNotFinished() {
        var player = new Player("Player1");
        var game = new Game(UUID.randomUUID(), 10, false, player, null);

        game.makeMove();
        assertEquals((10 + game.calculateMove()) / 3, game.getNumber());
        assertNull(game.getWinner());
        assertFalse(game.isFinished());
    }

    @Test
    void testMakeMoveGameFinished() {
        var player = new Player("Player1");
        var game = new Game(UUID.randomUUID(), 1, true, player, player);

        assertThrows(IllegalStateException.class, game::makeMove);
    }

    @Test
    void testMakeMoveGameEnds() {
        var player = new Player("Player1");
        var game = new Game(UUID.randomUUID(), 2, false, player, null);

        game.makeMove();
        assertEquals(1, game.getNumber());
        assertEquals(player, game.getWinner());
        assertTrue(game.isFinished());
    }

    @Test
    void testCalculateMove() {
        Player player = new Player("Player1");
        Game game = new Game(UUID.randomUUID(), 10, false, player, null);

        assertEquals(-1, game.calculateMove());

        game.setNumber(9);
        assertEquals(0, game.calculateMove());

        game.setNumber(8);
        assertEquals(1, game.calculateMove());
    }
}
