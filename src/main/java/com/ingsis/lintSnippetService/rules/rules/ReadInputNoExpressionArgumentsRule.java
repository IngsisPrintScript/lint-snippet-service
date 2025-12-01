package com.ingsis.lintSnippetService.rules.rules;

import com.ingsis.lintSnippetService.rules.LintRule;

public class ReadInputNoExpressionArgumentsRule implements LintRule {
    @Override
    public String getName() {
        return "readInputNoExpressionArgumentsRule";
    }

    @Override
    public boolean apply(String code) {
        String[] lines = code.split("\n");
        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty())
                continue;

            if (line.contains("readInput(")) {
                int startIndex = line.indexOf("readInput(") + "readInput(".length();
                int endIndex = line.lastIndexOf(")");

                if (endIndex <= startIndex) {
                    return false;
                }

                String inside = line.substring(startIndex, endIndex).trim();

                if (!inside.matches("[a-zA-Z_][a-zA-Z0-9_]*|\".*\"")) {
                    return false;
                }
            }
        }
        return true;
    }
}
