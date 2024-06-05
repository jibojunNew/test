package com.jae.playergames.controller;

import com.jae.playergames.domain.Game;
import com.jae.playergames.domain.Move;
import com.jae.playergames.domain.Player;
import com.jae.playergames.model.response.CommonApiResp;
import com.jae.playergames.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    @Value("${player.name}")
    private String playerName;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public ResponseEntity<CommonApiResp<Game>> startGame(@RequestParam int number) {
        Player player = new Player(playerName);
        Game game = gameService.createGame(number, player);
        return new ResponseEntity<>(new CommonApiResp<>(HttpStatus.CREATED.value(), "Game started", game), HttpStatus.CREATED);
    }

    @PostMapping("/{gameId}/moves")
    public ResponseEntity<CommonApiResp<Move>> makeMove(@PathVariable UUID gameId) {
        try {
            Move move = gameService.makeMove(gameId);
            return new ResponseEntity<>(new CommonApiResp<>(HttpStatus.CREATED.value(), "Move made", move), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(new CommonApiResp<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<CommonApiResp<Game>> getCurrentGame(@PathVariable UUID gameId) {
        Game game = gameService.getGame(gameId);
        if (game == null) {
            return new ResponseEntity<>(new CommonApiResp<>(HttpStatus.NOT_FOUND.value(), "Game not found", null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new CommonApiResp<>(HttpStatus.OK.value(), "Current game", game), HttpStatus.OK);
    }

}
