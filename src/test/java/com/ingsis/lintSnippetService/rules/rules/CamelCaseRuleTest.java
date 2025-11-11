package com.ingsis.lintSnippetService.rules.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CamelCaseRuleTest {

  private CamelCaseRule rule;

  @BeforeEach
  void setup() {
    rule = new CamelCaseRule();
  }

  @Test
  void givenCamelCaseTokens_whenApply_thenReturnTrue() {
    String code = "let myVar = 1; println(\"x\");";
    boolean result = rule.apply(code);
    assertTrue(result);
  }

  @Test
  void givenNonCamel_whenApply_thenReturnFalse() {
    String code = "let my_var = 2;";
    boolean result = rule.apply(code);
    assertFalse(result);
  }
}
