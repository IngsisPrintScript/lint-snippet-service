package com.ingsis.lintSnippetService.rules.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SnakeCaseRuleTest {

  private SnakeCaseRule rule;

  @BeforeEach
  void setup() {
    rule = new SnakeCaseRule();
  }

  @Test
  void givenSnakeCaseTokens_whenApply_thenReturnTrue() {
    String code = "let my_var = 1; println(\"ok\");";
    boolean result = rule.apply(code);
    assertTrue(result);
  }

  @Test
  void givenNonSnake_whenApply_thenReturnFalse() {
    String code = "let myVar = 2;";
    boolean result = rule.apply(code);
    assertFalse(result);
  }
}
