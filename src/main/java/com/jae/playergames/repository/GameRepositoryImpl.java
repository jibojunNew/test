package com.jae.playergames.repository;

import com.jae.playergames.domain.Game;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class GameRepositoryImpl implements GameRepository {

    private RedisTemplate<String, Game> redisTemplate;

    private static final String KEY_PREFIX = "testGame-";

    @Override
    public void save(Game game) {
        redisTemplate.opsForValue().set(KEY_PREFIX + game.getId(), game);
    }

    @Override
    public Game getById(UUID gameId) {
        return redisTemplate.opsForValue().get(KEY_PREFIX + gameId);
    }

    @Override
    public void deleteAll() {
        redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys(KEY_PREFIX + "*")));
    }
}
