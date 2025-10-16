package com.ingsis.lintSnippetService.rules;

import com.ingsis.lintSnippetService.rules.rules.CamelCaseRule;
import com.ingsis.lintSnippetService.rules.rules.PrintlnNoExpressionArguments;
import com.ingsis.lintSnippetService.rules.rules.ReadInputNoExpressionArgumentsRule;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import com.ingsis.lintSnippetService.rules.rules.SnakeCaseRule;
import org.springframework.stereotype.Component;

@Component
public class RuleRegistry {

  private final Map<String, LintRule> rules = new HashMap<>();

  public RuleRegistry() {
    rules.put("printlnNoExpressionArguments", new PrintlnNoExpressionArguments());
    rules.put("readInputNoExpressionArguments", new ReadInputNoExpressionArgumentsRule());
    rules.put("snakeCase",new SnakeCaseRule());
    rules.put("camelCase",new CamelCaseRule());
  }

  public LintRule getRule(String name) {
    try {
      return rules.get(name);
    }catch (Exception e){
      throw new NoSuchElementException("rule not found");
    }
  }
}
