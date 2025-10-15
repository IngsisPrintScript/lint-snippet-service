package com.ingsis.lintSnippetService.rules;

import com.ingsis.lintSnippetService.rules.rules.IdentifierCasingRule;
import com.ingsis.lintSnippetService.rules.rules.PrintlnNoExpressionArguments;
import com.ingsis.lintSnippetService.rules.rules.ReadInputNoExpressionArgumentsRule;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Component;

@Component
public class RuleRegistry {

  private final Map<String, LintRule> rules = new HashMap<>();

  public RuleRegistry() {
    rules.put("printlnNoExpressionArguments", new PrintlnNoExpressionArguments());
    rules.put("readInputNoExpressionArgumentsRule", new ReadInputNoExpressionArgumentsRule());
    rules.put("identifierCasing", new IdentifierCasingRule());
  }

  public LintRule getRule(String name) {
    try {
      return rules.get(name);
    }catch (Exception e){
      throw new NoSuchElementException("rule not found");
    }
  }
}
