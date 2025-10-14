package com.ingsis.lintSnippetService.linting.dto;

import java.util.UUID;

public record UpdateLintingDTO(UUID lintId, String value, boolean active){
}
