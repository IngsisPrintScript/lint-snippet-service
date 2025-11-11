package com.ingsis.lintSnippetService.redis;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingsis.lintSnippetService.redis.dto.LintResultEvent;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;

class LintResultProducerTest {

  private RedisTemplate<String, String> redisTemplate;

  @SuppressWarnings("rawtypes")
  private org.springframework.data.redis.core.StreamOperations streamOperations;

  private ObjectMapper objectMapper;
  private LintResultProducer producer;

  @BeforeEach
  void setup() {
    redisTemplate = mock(RedisTemplate.class);
    streamOperations = mock(org.springframework.data.redis.core.StreamOperations.class);
    when(redisTemplate.opsForStream()).thenReturn(streamOperations);
    objectMapper = new ObjectMapper();
    producer = new LintResultProducer("stream", redisTemplate, objectMapper);
  }

  @Test
  void givenEvent_whenPublish_thenOpsForStreamCalled() {
    var event =
        new LintResultEvent(
            "u", UUID.randomUUID(), com.ingsis.lintSnippetService.redis.dto.LintStatus.PASSED);
    producer.publish(event);
    verify(redisTemplate, atLeastOnce()).opsForStream();
  }

  @Test
  void givenMapperThrows_whenPublish_thenNoExceptionAndNoStreamCall() {
    RedisTemplate<String, String> rt = mock(RedisTemplate.class);
    var mapper = mock(ObjectMapper.class);
    try {
      when(mapper.writeValueAsString(any()))
          .thenThrow(new com.fasterxml.jackson.core.JsonProcessingException("boom") {});
    } catch (Exception ex) {
    }
    var p = new LintResultProducer("s", rt, mapper);
    var event =
        new LintResultEvent(
            "u", UUID.randomUUID(), com.ingsis.lintSnippetService.redis.dto.LintStatus.FAILED);

    org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> p.publish(event));
    verify(rt, never()).opsForStream();
  }
}
