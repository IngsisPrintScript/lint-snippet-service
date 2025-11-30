package com.ingsis.lintSnippetService.redis.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RedisConfigTest {

  @Test
  void beansAreCreated() {
    RedisConfig cfg = new RedisConfig();
    var factory = cfg.redisConnectionFactory();
    assertNotNull(factory);
    var template = cfg.redisTemplate(factory);
    assertNotNull(template);
  }
}
