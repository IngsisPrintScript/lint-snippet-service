package com.ingsis.lintSnippetService.redis.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class LintResultEventTest {

  @Test
  void givenRecord_whenAccessors_thenMatch() {
    UUID id = UUID.randomUUID();
    var r = new LintResultEvent("u", id, LintStatus.PASSED);
    assertEquals("u", r.userId());
    assertEquals(id, r.snippetId());
    assertEquals(LintStatus.PASSED, r.status());
  }
}
