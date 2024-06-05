package com.jae.playergames.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.UUID;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class Game implements Serializable {

    private UUID id;

    private int number;

    private boolean finished;

    private Player currentPlayer;

    private Player winner;

    public Move makeMove() {
        if (finished) {
            throw new IllegalStateException("Game is already finished.");
        }

        int moveValue = calculateMove();
        number = (number + moveValue) / 3;

        Move move = new Move(currentPlayer, moveValue, number);

        if (number == 1) {
            winner = currentPlayer;
            finished = true;
        }

        return move;
    }

    public int calculateMove() {
        if (number % 3 == 0) {
            return 0;
        } else if ((number + 1) % 3 == 0) {
            return 1;
        } else {
            return -1;
        }
    }

}
