package com.ingsis.lintSnippetService.rules;

import com.ingsis.lintSnippetService.rules.rules.IdentifierCasingRule;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RuleRegistry {

    private final Map<String, LintRule> rules = new HashMap<>();

    public RuleRegistry() {
        rules.put("spaceBeforeColon", new SpaceBeforeColonRule());
        rules.put("identifierCasing", new IdentifierCasingRule());}

    public LintRule getRule(String name) {
        return rules.get(name);
    }
}