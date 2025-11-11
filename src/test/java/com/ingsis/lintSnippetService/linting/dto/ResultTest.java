package com.ingsis.lintSnippetService.linting.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.ingsis.lintSnippetService.redis.dto.LintStatus;
import org.junit.jupiter.api.Test;

class ResultTest {

  @Test
  void givenRecord_whenAccessors_thenMatch() {
    var dto = new Result(LintStatus.FAILED, "r");
    assertEquals(LintStatus.FAILED, dto.evaluated());
    assertEquals("r", dto.nameRule());
  }
}
