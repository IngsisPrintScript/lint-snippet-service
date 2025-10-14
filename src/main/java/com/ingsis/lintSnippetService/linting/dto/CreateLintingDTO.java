package com.ingsis.lintSnippetService.linting.dto;

public record CreateLintingDTO(String name, String defaultValue, boolean active,
                               String ownerId) {}