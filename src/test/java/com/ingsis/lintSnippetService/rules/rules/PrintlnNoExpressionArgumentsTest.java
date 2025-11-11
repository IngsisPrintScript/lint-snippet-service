package com.ingsis.lintSnippetService.rules.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PrintlnNoExpressionArgumentsTest {

  private PrintlnNoExpressionArguments rule;

  @BeforeEach
  void setup() {
    rule = new PrintlnNoExpressionArguments();
  }

  @Test
  void givenSimplePrintln_whenApply_thenReturnTrue() {
    String code = "println(\"hello\");\nprintln(x);";
    boolean result = rule.apply(code);
    assertTrue(result);
  }

  @Test
  void givenExpressionInPrintln_whenApply_thenReturnFalse() {
    String code = "println(a + b);";
    boolean result = rule.apply(code);
    assertFalse(result);
  }
}
