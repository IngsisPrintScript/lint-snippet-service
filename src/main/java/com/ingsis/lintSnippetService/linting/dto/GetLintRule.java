package com.ingsis.lintSnippetService.linting.dto;

import java.util.UUID;

public record GetLintRule(UUID lintId, String name, boolean active) {}
