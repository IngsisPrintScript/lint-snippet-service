package com.ingsis.lintSnippetService.linting.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class GetLintRuleTest {

  @Test
  void givenRecord_whenAccessors_thenMatch() {
    UUID id = UUID.randomUUID();
    var dto = new GetLintRule(id, "r", true);
    assertEquals(id, dto.lintId());
    assertEquals("r", dto.name());
    assertTrue(dto.active());
  }
}
