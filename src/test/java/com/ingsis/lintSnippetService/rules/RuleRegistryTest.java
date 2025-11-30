package com.ingsis.lintSnippetService.rules;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RuleRegistryTest {

  private RuleRegistry registry;

  @BeforeEach
  void setup() {
    registry = new RuleRegistry();
  }

  @Test
  void givenKnownRule_whenGetRule_thenNotNull() {
    String name = "camelCase";
    var rule = registry.getRule(name);
    assertNotNull(rule);
    assertEquals("camelCase", rule.getName());
  }

  @Test
  void givenUnknownRule_whenGetRule_thenReturnsNull() {
    String name = "unknown";
    var rule = registry.getRule(name);
    assertNull(rule);
  }
}
