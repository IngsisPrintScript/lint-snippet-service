package com.ingsis.lintSnippetService.linting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ingsis.lintSnippetService.linting.dto.EvaluateSnippet;
import com.ingsis.lintSnippetService.linting.dto.Result;
import com.ingsis.lintSnippetService.redis.dto.LintStatus;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class LintingControllerTest {

  @Mock private LintingService service;

  private LintingController controller;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    controller = new LintingController(service);
  }

  @Test
  void givenEvaluate_whenNoResults_thenReturnPassedStatus() {
    var dto = new EvaluateSnippet("code", "owner");
    when(service.evaluate(dto.content(), dto.ownerId())).thenReturn(ResponseEntity.ok(List.of()));
    ResponseEntity<LintStatus> res = controller.evaluateLintingRules(dto);
    assertEquals(LintStatus.PASSED, res.getBody());
  }

  @Test
  void givenEvaluate_whenHasResults_thenReturnFailedStatus() {
    var dto = new EvaluateSnippet("code", "owner");
    when(service.evaluate(dto.content(), dto.ownerId()))
        .thenReturn(ResponseEntity.ok(List.of(new Result(LintStatus.FAILED, "r"))));
    ResponseEntity<LintStatus> res = controller.evaluateLintingRules(dto);
    assertEquals(LintStatus.FAILED, res.getBody());
  }
}
