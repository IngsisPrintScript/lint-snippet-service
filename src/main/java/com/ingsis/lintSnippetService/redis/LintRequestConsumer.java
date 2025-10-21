package com.ingsis.lintSnippetService.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingsis.lintSnippetService.linting.LintingService;
import com.ingsis.lintSnippetService.linting.dto.Result;
import com.ingsis.lintSnippetService.redis.dto.LintRequestEvent;
import com.ingsis.lintSnippetService.redis.dto.LintResultEvent;
import com.ingsis.lintSnippetService.redis.dto.LintStatus;
import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.austral.ingsis.redis.RedisStreamConsumer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class LintRequestConsumer extends RedisStreamConsumer<String> {

  private static final Logger logger = LoggerFactory.getLogger(LintRequestConsumer.class);

  private final LintingService lintingService;
  private final LintResultProducer lintResultProducer;
  private final ObjectMapper objectMapper;
  private final ExecutorService executor = Executors.newFixedThreadPool(10);

  public LintRequestConsumer(
      @Value("${redis.streams.lintRequest}") String streamName,
      @Value("${redis.groups.lint}") String groupName,
      RedisTemplate<String, String> redisTemplate,
      LintingService lintingService,
      LintResultProducer lintResultProducer,
      ObjectMapper objectMapper) {

    super(streamName, groupName, redisTemplate);
    this.lintingService = lintingService;
    this.lintResultProducer = lintResultProducer;
    this.objectMapper = objectMapper;
  }

  @Override
  public void onMessage(@NotNull ObjectRecord<String, String> record) {
    executor.submit(
        () -> {
          try {
            LintRequestEvent event =
                objectMapper.readValue(record.getValue(), LintRequestEvent.class);
            logger.info(
                "Processing lint request for Snippet({}) from User({})",
                event.snippetId().toString(),
                event.ownerId());

            ResponseEntity<List<Result>> response =
                lintingService.evaluate(event.content(), event.ownerId());
            List<Result> results = response.getBody();
            LintStatus status =
                (results == null || results.isEmpty()) ? LintStatus.PASSED : LintStatus.FAILED;

            lintResultProducer.publish(
                new LintResultEvent(event.ownerId(), event.snippetId(), status));
          } catch (Exception ex) {
            logger.error("Error processing lint request: {}", ex.getMessage());
          }
          return null;
        });
  }

  @Override
  public @NotNull StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, String>>
      options() {
    return StreamReceiver.StreamReceiverOptions.builder()
        .pollTimeout(java.time.Duration.ofSeconds(10))
        .targetType(String.class)
        .build();
  }

  @PreDestroy
  public void shutdown() {
    executor.shutdown();
  }
}
