package com.ingsis.lintSnippetService.rules;

public interface LintRule {
  String getName();

  boolean apply(String code, String value);
}
