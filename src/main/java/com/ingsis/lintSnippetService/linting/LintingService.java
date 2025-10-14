package com.ingsis.lintSnippetService.linting;

import com.ingsis.lintSnippetService.linting.dto.CreateLintingDTO;
import com.ingsis.lintSnippetService.linting.dto.Result;
import com.ingsis.lintSnippetService.linting.dto.UpdateLintingDTO;
import com.ingsis.lintSnippetService.rules.LintRule;
import com.ingsis.lintSnippetService.rules.RuleRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LintingService {

  private final LintingRepository lintingRepository;
  private final RuleRegistry ruleRegistry;

  public LintingService(RuleRegistry registry, LintingRepository lintingRepository) {
    this.lintingRepository = lintingRepository;
    this.ruleRegistry = registry;
  }

  public ResponseEntity<Void> saveRules(List<CreateLintingDTO> ruleDTOs, UUID ymlId) {
    List<Lint> lints = new ArrayList<>();
    for (CreateLintingDTO dto : ruleDTOs) {
      Lint rule = lintingRepository.findByName((dto.name()));
      if (rule == null) {
        rule = new Lint(dto.ownerId(), ymlId, dto.name(), dto.defaultValue(), dto.active());
        lints.add(rule);
      }
    }
    lintingRepository.saveAll(lints);
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<?> updateRule(List<UpdateLintingDTO> updateLintingDTO) {
    for (UpdateLintingDTO dto : updateLintingDTO) {
      Lint result =
          lintingRepository
              .findById(dto.lintId())
              .orElseThrow(() -> new RuntimeException(dto.lintId() + " not found"));
      result.setActive(dto.active());
      result.setDefaultValue(dto.value());
      lintingRepository.save(result);
    }
    return ResponseEntity.badRequest().build();
  }

  public ResponseEntity<List<Result>> evaluate(String content, String ownerId) {
    List<Lint> rules = lintingRepository.findByOwnerIdAndActive(ownerId, true);
    List<Result> invalidRules = new ArrayList<>();
    for (Lint lint : rules) {
      LintRule rule = ruleRegistry.getRule(lint.getName());
      if (rule != null) {
        if (!rule.apply(content, lint.getDefaultValue())) {
          invalidRules.add(new Result(false, rule.getName()));
        }
      }
    }
    return ResponseEntity.ok(invalidRules);
  }
}
