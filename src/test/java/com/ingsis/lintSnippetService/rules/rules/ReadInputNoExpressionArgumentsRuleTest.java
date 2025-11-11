package com.ingsis.lintSnippetService.rules.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReadInputNoExpressionArgumentsRuleTest {

  private ReadInputNoExpressionArgumentsRule rule;

  @BeforeEach
  void setup() {
    rule = new ReadInputNoExpressionArgumentsRule();
  }

  @Test
  void givenValidReadInput_whenApply_thenReturnTrue() {
    String code = "let x = readInput(\"msg\");\nreadInput(a);";
    boolean result = rule.apply(code);
    assertTrue(result);
  }

  @Test
  void givenExpressionInsideReadInput_whenApply_thenReturnFalse() {
    String code = "readInput(a + b);";
    boolean result = rule.apply(code);
    assertFalse(result);
  }

  @Test
  void givenMissingParen_whenApply_thenReturnFalse() {
    String code = "readInput(";
    boolean result = rule.apply(code);
    assertFalse(result);
  }
}
