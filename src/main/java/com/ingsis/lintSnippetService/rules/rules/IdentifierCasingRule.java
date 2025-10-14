package com.ingsis.lintSnippetService.rules.rules;

import com.ingsis.lintSnippetService.rules.LintRule;

public class IdentifierCasingRule implements LintRule {
  @Override
  public String getName() {
    return "identifierCasing";
  }

  @Override
  public boolean apply(String code, String value) {
    String[] lines = code.split("\n");
    for (String line : lines) {
      String trimmed = line.trim();
      if (trimmed.startsWith("println(") && trimmed.endsWith(")")) {
        String inside = trimmed.substring(8, trimmed.length() - 1).trim();
        boolean isIdentifier = inside.matches("[a-zA-Z_][a-zA-Z0-9_]*");
        boolean isStringLiteral = inside.matches("\".*\"");
        boolean isNumber = inside.matches("\\d+");
        if (!(isIdentifier || isStringLiteral || isNumber)) {
          return false;
        }
      }
    }
    return true;
  }
}
