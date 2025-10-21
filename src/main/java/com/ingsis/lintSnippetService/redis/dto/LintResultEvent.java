package com.ingsis.lintSnippetService.redis.dto;

import java.util.UUID;

public record LintResultEvent(String userId, UUID snippetId, LintStatus status) {}
