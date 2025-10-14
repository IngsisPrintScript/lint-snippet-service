package com.ingsis.lintSnippetService.rules.rules;

import com.ingsis.lintSnippetService.rules.LintRule;

public class PrintlnNoExpressionArguments implements LintRule {
  @Override
  public String getName() {
    return "printlnNoExpressionArguments";
  }

  @Override
  public boolean apply(String code, String value) {
    String[] lines = code.split("\n");
    for (String s : lines) {
      String line = s.trim();
      if (line.isEmpty()) continue;
      if (!line.startsWith("println(") || !line.endsWith(")")) {
        return false;
      }
      String inside = line.substring(8, line.length() - 1).trim();
      if (!inside.matches("[a-zA-Z_][a-zA-Z0-9_]*|\".*\"|\\d+")) {
        return false;
      }
    }
    return true;
  }
}
