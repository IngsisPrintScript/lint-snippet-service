package com.ingsis.lintSnippetService.linting;

import com.ingsis.lintSnippetService.linting.dto.CreateLintingDTO;
import com.ingsis.lintSnippetService.linting.dto.Result;
import com.ingsis.lintSnippetService.linting.dto.UpdateLintingDTO;
import com.ingsis.lintSnippetService.redis.dto.LintStatus;
import com.ingsis.lintSnippetService.rules.LintRule;
import com.ingsis.lintSnippetService.rules.RuleRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LintingService {

  private final LintingRepository lintingRepository;
  private final RuleRegistry ruleRegistry;
  private static final Logger logger = LoggerFactory.getLogger(LintingService.class);

  public LintingService(RuleRegistry registry, LintingRepository lintingRepository) {
    this.lintingRepository = lintingRepository;
    this.ruleRegistry = registry;
  }

  public ResponseEntity<Void> saveRules(List<CreateLintingDTO> ruleDTOs, String ownerId) {
    List<Lint> lints = new ArrayList<>();
    for (CreateLintingDTO dto : ruleDTOs) {
      Lint rule = lintingRepository.findByNameAndOwnerId(dto.name(), ownerId);
      if (rule == null) {
        rule = new Lint(ownerId, dto.name(), dto.defaultValue(), dto.active());
        logger.info("Created rule {} for owner {}", dto.name(), ownerId);
        if (!lints.contains(rule)){
          lints.add(rule);
        }logger.info("Rule {} already declared for owner {}", rule, ownerId);
      }
      logger.info("rule {} already exists for owner {}", dto.name(), ownerId);
    }
    lintingRepository.saveAll(lints);
    logger.info("Saving {} rules for owner {}", lints.size(), ownerId);
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<?> updateRule(List<UpdateLintingDTO> updateLintingDTO, String ownerId) {
    for (UpdateLintingDTO dto : updateLintingDTO) {
      Lint result =
          lintingRepository
              .findByOwnerIdAndId(ownerId,dto.lintId());
      if(result == null){
        logger.info("Rule {} not found for ownerId {}", dto.lintId(), ownerId);
        return ResponseEntity.badRequest().body(dto.lintId() + " not found");
      }
      result.setActive(dto.active());
      logger.info("{} rule", dto.active() ? "Enabled" : "Disabled");
      result.setDefaultValue(dto.value());
      logger.info("Updated value to {} for ownerId {}", dto.value(), ownerId);
      lintingRepository.save(result);
      logger.info("Updated rule {} for ownerId {}", dto.lintId(), ownerId);
    }
    return ResponseEntity.ok().build();
  }

  public ResponseEntity<List<Result>> evaluate(String content, String ownerId) {
    logger.info("Code to lint {}", content);
    List<Lint> rules = lintingRepository.findByOwnerIdAndActive(ownerId, true);
    logger.info("Found {} active rules for owner {}", rules.size(), ownerId);
    List<Result> invalidRules = new ArrayList<>();
    for (Lint lint : rules) {
      LintRule rule = ruleRegistry.getRule(lint.getName());
      if (rule == null) {
        logger.warn("Rule {} not found in registry", lint.getName());
        continue;
      }
      boolean passed = rule.apply(content);
      logger.info("Applying rule {} to snippet: {}", rule.getName(), passed ? "PASSED" : "FAILED");

      if (!passed) {
        invalidRules.add(new Result(LintStatus.FAILED, rule.getName()));
      }
    }
    logger.info("Lint evaluation completed for owner {}. Invalid rules: {}", ownerId, invalidRules);
    return ResponseEntity.ok(Collections.unmodifiableList(invalidRules));
  }
}
