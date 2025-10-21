package com.ingsis.lintSnippetService.linting;

import com.ingsis.lintSnippetService.linting.dto.CreateLintingDTO;
import com.ingsis.lintSnippetService.linting.dto.EvaluateSnippet;
import com.ingsis.lintSnippetService.linting.dto.Result;
import com.ingsis.lintSnippetService.linting.dto.UpdateLintingDTO;
import com.ingsis.lintSnippetService.redis.dto.LintStatus;
import java.util.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/linting")
public class LintingController {

  private final LintingService lintingService;

  public LintingController(LintingService lintingService) {
    this.lintingService = lintingService;
  }

  @PostMapping("/create")
  public ResponseEntity<Void> createLintRule(
      @RequestBody List<CreateLintingDTO> lintingDTO, @RequestParam String ownerId) {
    lintingService.saveRules(lintingDTO, ownerId);
    return ResponseEntity.ok().build();
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateLintRule(
      @RequestBody List<UpdateLintingDTO> updateLintingDTO, @RequestParam String ownerId) {
    try {
      return ResponseEntity.ok(lintingService.updateRule(updateLintingDTO, ownerId));
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PostMapping("/evaluate")
  public ResponseEntity<LintStatus> evaluateLintingRules(
      @RequestBody EvaluateSnippet evaluateSnippet) {
    List<Result> result =
        lintingService.evaluate(evaluateSnippet.content(), evaluateSnippet.ownerId()).getBody();
    if (result == null || result.isEmpty()) {
      return ResponseEntity.ok(LintStatus.PASSED);
    }
    return ResponseEntity.ok(LintStatus.FAILED);
  }

  @PostMapping("/evaluate/pass")
  public ResponseEntity<List<Result>> evaluateSnippet(
      @RequestBody EvaluateSnippet evaluateSnippet) {
    List<Result> result =
        lintingService.evaluate(evaluateSnippet.content(), evaluateSnippet.ownerId()).getBody();
    return ResponseEntity.ok(Objects.requireNonNullElseGet(result, List::of));
  }
}
