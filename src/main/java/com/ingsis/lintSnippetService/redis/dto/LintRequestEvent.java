package com.ingsis.lintSnippetService.redis.dto;

import java.util.UUID;

public record LintRequestEvent(String ownerId, UUID snippetId, String language, String content) {
}