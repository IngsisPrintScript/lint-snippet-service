package com.ingsis.lintSnippetService.redis.dto;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LintStatusTest {

  @Test
  void enumHasValues() {
    var vals = LintStatus.values();
    assertTrue(vals.length >= 2);
    assertEquals(LintStatus.PASSED, LintStatus.valueOf("PASSED"));
    assertEquals(LintStatus.FAILED, LintStatus.valueOf("FAILED"));
  }
}
