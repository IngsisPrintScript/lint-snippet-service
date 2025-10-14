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
            if (!(line.startsWith("println(") && line.length() > 8)) {
                return false;
            }
        }
        return true;
    }
}
