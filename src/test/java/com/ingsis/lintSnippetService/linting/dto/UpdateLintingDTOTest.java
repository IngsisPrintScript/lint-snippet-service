package com.ingsis.lintSnippetService.linting.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class UpdateLintingDTOTest {

  @Test
  void givenRecord_whenAccessors_thenMatch() {
    UUID id = UUID.randomUUID();
    var dto = new UpdateLintingDTO(id, "v", false);
    assertEquals(id, dto.lintId());
    assertEquals("v", dto.value());
    assertFalse(dto.active());
  }
}
