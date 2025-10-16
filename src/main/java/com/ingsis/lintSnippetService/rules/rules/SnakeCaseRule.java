package com.ingsis.lintSnippetService.rules.rules;

import com.ingsis.lintSnippetService.rules.LintRule;

import java.util.Set;

public class SnakeCaseRule implements LintRule {

    @Override
    public String getName() {
        return "snakeCase";
    }

    private static final Set<String> IGNORED_WORDS = Set.of(
            "if", "else", "let", "const", "println",
            "string", "String", "number", "Number", "boolean"
    );

    @Override
    public boolean apply(String code) {
        String[] tokens = code.split("[\\s;=(){}]+");

        for (String token : tokens) {
            token = token.trim();
            if (token.isEmpty()) continue;
            if (token.matches("\\d+")) continue;
            if (token.matches("\".*\"")) continue;
            if (IGNORED_WORDS.contains(token)) continue;
            if (token.matches("[a-zA-Z_][a-zA-Z0-9_]*")) {
                boolean isSnakeCase = token.matches("[a-z]+(_[a-z0-9]+)*");
                if (!isSnakeCase) {
                    return false;
                }
            }
        }
        return true;
    }
}
