package com.ingsis.lintSnippetService.linting;

import com.ingsis.lintSnippetService.linting.dto.CreateLintingDTO;
import com.ingsis.lintSnippetService.linting.dto.EvaluateSnippet;
import com.ingsis.lintSnippetService.linting.dto.Result;
import com.ingsis.lintSnippetService.linting.dto.UpdateLintingDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/linting")
public class LintingController {

    private final LintingService lintingService;

    public LintingController(LintingService lintingService) {
        this.lintingService = lintingService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createLintRule(@RequestBody List<CreateLintingDTO> lintingDTO){
        UUID ymlId = UUID.randomUUID();
        try {
            lintingService.saveRules(lintingDTO, ymlId);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateLintRule(@RequestBody List<UpdateLintingDTO> updateLintingDTO){
        try {
            return ResponseEntity.ok(lintingService.updateRule(updateLintingDTO));
        }catch(Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/evaluate")
    public ResponseEntity<Void> evaluateSnippet(@RequestBody EvaluateSnippet evaluateSnippet){
        Result result = lintingService.evaluate(evaluateSnippet.content(),evaluateSnippet.ownerId()).getBody();
        return  ResponseEntity.ok().build();
    }

}
