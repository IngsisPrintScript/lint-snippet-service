package com.ingsis.lintSnippetService.linting.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CreateLintingDTOTest {

  @Test
  void givenRecord_whenAccessors_thenMatch() {
    var dto = new CreateLintingDTO("r1", "def", true);
    assertEquals("r1", dto.name());
    assertEquals("def", dto.defaultValue());
    assertTrue(dto.active());
  }
}
