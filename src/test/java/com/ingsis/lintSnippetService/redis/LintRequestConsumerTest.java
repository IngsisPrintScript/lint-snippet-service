package com.ingsis.lintSnippetService.redis;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingsis.lintSnippetService.linting.LintingService;
import com.ingsis.lintSnippetService.redis.dto.LintRequestEvent;
import com.ingsis.lintSnippetService.redis.dto.LintResultEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.stream.ObjectRecord;

class LintRequestConsumerTest {

  @Mock private LintingService lintingService;
  @Mock private LintResultProducer producer;

  private ObjectMapper objectMapper;
  private LintRequestConsumer consumer;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    objectMapper = mock(ObjectMapper.class);
    org.springframework.data.redis.core.RedisTemplate<String, String> redisTemplate =
        mock(org.springframework.data.redis.core.RedisTemplate.class);
    consumer =
        new LintRequestConsumer("s", "g", redisTemplate, lintingService, producer, objectMapper);
  }

  @Test
  void optionsAndShutdown_doNotThrow() {
    var opts = consumer.options();
    consumer.shutdown();
    assertNotNull(opts);
  }

  @Test
  void options_haveExpectedTimeoutAndTargetType() {
    var opts = consumer.options();
    assertNotNull(opts);
    assertEquals(java.time.Duration.ofSeconds(10), opts.getPollTimeout());
    assertEquals(String.class, opts.getTargetType());
  }

  @Test
  void givenInvalidJson_whenOnMessage_thenProducerNotCalled() throws Exception {
    ObjectRecord<String, String> record = mock(ObjectRecord.class);
    when(record.getValue()).thenReturn("bad-json");
    when(objectMapper.readValue(any(String.class), eq(LintRequestEvent.class)))
        .thenThrow(new RuntimeException("parse error"));
    consumer.onMessage(record);
    Thread.sleep(100);
    verify(producer, never()).publish(any(LintResultEvent.class));
  }
}
