package com.jae.playergames.integrationtest;

import com.jae.playergames.domain.Game;
import com.jae.playergames.domain.Player;
import com.jae.playergames.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestPropertySource(locations = "classpath:application-player1-test.yaml")
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameRepository gameRepository;

    private Game game;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", () -> "localhost:9092");
        registry.add("spring.redis.host", () -> "localhost");
        registry.add("spring.redis.port", () -> "6379");
        registry.add("player.name", () -> "Player1");
    }


    @BeforeEach
    public void setup() {
        gameRepository.deleteAll();
        Player player = new Player("Player1");
        game = new Game(UUID.randomUUID(), 15, false, player, null);
        gameRepository.save(game);
    }

    @Test
    void testStartGame() throws Exception {
        mockMvc.perform(post("/games")
                        .param("number", "15")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Game started"))
                .andExpect(jsonPath("$.data.number").value(15));
    }

    @Test
    void testMakeMove() throws Exception {
        mockMvc.perform(post("/games/" + game.getId() + "/moves")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(jsonPath("$.message").value("Move made"))
                .andExpect(jsonPath("$.data.result").value((15 + game.calculateMove()) / 3));
    }

    @Test
    void testGetCurrentGame() throws Exception {
        mockMvc.perform(get("/games/" + game.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Current game"))
                .andExpect(jsonPath("$.data.number").value(15));
    }

    @Test
    void testGetNonexistentGame() throws Exception {
        UUID nonexistentGameId = UUID.randomUUID();
        mockMvc.perform(get("/games/" + nonexistentGameId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Game not found"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testMakeMoveOnFinishedGame() throws Exception {
        game.setFinished(true);
        gameRepository.save(game);

        mockMvc.perform(post("/games/" + game.getId() + "/moves")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Game not started or already finished."));
    }

}
