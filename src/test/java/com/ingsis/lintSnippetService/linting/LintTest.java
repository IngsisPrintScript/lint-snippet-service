package com.ingsis.lintSnippetService.linting;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LintTest {

  private Lint lint;

  @BeforeEach
  void setup() {
    lint = new Lint("owner1", "camelCase", "default", true);
  }

  @Test
  void givenLint_whenGetters_thenReturnValues() {

    assertEquals("owner1", lint.getOwnerId());
    assertEquals("camelCase", lint.getName());
    assertEquals("default", lint.getDefaultValue());
    assertTrue(lint.isActive());
  }

  @Test
  void givenSetters_whenCalled_thenChangeValues() {
    lint.setDefaultValue("x");
    lint.setActive(false);

    assertEquals("x", lint.getDefaultValue());
    assertFalse(lint.isActive());
  }
}
