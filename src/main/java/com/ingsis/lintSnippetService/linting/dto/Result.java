package com.ingsis.lintSnippetService.linting.dto;

import com.ingsis.lintSnippetService.redis.dto.LintStatus;

public record Result(LintStatus evaluated, String nameRule) {
}
