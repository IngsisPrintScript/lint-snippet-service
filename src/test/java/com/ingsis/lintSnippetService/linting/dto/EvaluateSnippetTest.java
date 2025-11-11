package com.ingsis.lintSnippetService.linting.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class EvaluateSnippetTest {

  @Test
  void givenRecord_whenAccessors_thenMatch() {
    var dto = new EvaluateSnippet("code", "owner");
    assertEquals("code", dto.content());
    assertEquals("owner", dto.ownerId());
  }
}
