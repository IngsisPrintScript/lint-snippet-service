package com.ingsis.lintSnippetService.redis.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class LintRequestEventTest {

  @Test
  void givenRecord_whenAccessors_thenMatch() {
    UUID id = UUID.randomUUID();
    var r = new LintRequestEvent("owner", id, "kt", "code");
    assertEquals("owner", r.ownerId());
    assertEquals(id, r.snippetId());
    assertEquals("kt", r.language());
    assertEquals("code", r.content());
  }
}
