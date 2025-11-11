package com.ingsis.lintSnippetService.linting;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.ingsis.lintSnippetService.linting.dto.CreateLintingDTO;
import com.ingsis.lintSnippetService.linting.dto.GetLintRule;
import com.ingsis.lintSnippetService.linting.dto.UpdateLintingDTO;
import com.ingsis.lintSnippetService.redis.dto.LintStatus;
import com.ingsis.lintSnippetService.rules.LintRule;
import com.ingsis.lintSnippetService.rules.RuleRegistry;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class LintingServiceTest {

  @Mock private LintingRepository repository;
  @Mock private RuleRegistry registry;

  private LintingService service;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    service = new LintingService(registry, repository);
  }

  @Test
  void givenNewRules_whenSaveRules_thenRepositorySaveAllCalled() {
    var dto = new CreateLintingDTO("r1", "v", true);
    when(repository.findByNameAndOwnerId("r1", "owner")).thenReturn(null);
    ResponseEntity<Void> res = service.saveRules(List.of(dto), "owner");
    assertEquals(200, res.getStatusCodeValue());
    verify(repository, atLeastOnce()).saveAll(any());
  }

  @Test
  void givenUpdateNonExisting_whenUpdateRule_thenBadRequest() {
    var dto = new UpdateLintingDTO(UUID.randomUUID(), "v", true);
    when(repository.findByOwnerIdAndId("owner", dto.lintId())).thenReturn(null);
    ResponseEntity<?> res = service.updateRule(List.of(dto), "owner");
    assertEquals(400, res.getStatusCodeValue());
  }

  @Test
  void givenFailingRule_whenEvaluate_thenReturnFailedResult() {
    Lint lint = new Lint("owner", "myRule", "d", true);
    when(repository.findByOwnerIdAndActive("owner", true)).thenReturn(List.of(lint));
    LintRule mockRule = mock(LintRule.class);
    when(mockRule.getName()).thenReturn("myRule");
    when(mockRule.apply(any())).thenReturn(false);
    when(registry.getRule("myRule")).thenReturn(mockRule);
    ResponseEntity<List<com.ingsis.lintSnippetService.linting.dto.Result>> res =
        service.evaluate("x", "owner");
    assertEquals(200, res.getStatusCodeValue());
    var body = res.getBody();
    assertNotNull(body);
    assertFalse(body.isEmpty());
    assertEquals(LintStatus.FAILED, body.get(0).evaluated());
  }

  @Test
  void givenExistingRule_whenSaveRules_thenDoNotAddDuplicate() {
    var dto = new CreateLintingDTO("r1", "v", true);
    when(repository.findByNameAndOwnerId("r1", "owner"))
        .thenReturn(new Lint("owner", "r1", "v", true));
    ResponseEntity<Void> res = service.saveRules(List.of(dto, dto), "owner");
    assertEquals(200, res.getStatusCodeValue());
    // saveAll may be called with empty list or not, but should not throw
    verify(repository, atMostOnce()).saveAll(any());
  }

  @Test
  void givenUpdateExisting_whenUpdateRule_thenSaveCalled() {
    UUID id = UUID.randomUUID();
    var dto = new UpdateLintingDTO(id, "newv", false);
    Lint stored = new Lint("owner", "r", "v", true);
    when(repository.findByOwnerIdAndId("owner", id)).thenReturn(stored);
    ResponseEntity<?> res = service.updateRule(List.of(dto), "owner");
    assertEquals(200, res.getStatusCodeValue());
    verify(repository, times(1)).save(stored);
    assertFalse(stored.isActive());
    assertEquals("newv", stored.getDefaultValue());
  }

  @Test
  void givenGetAllByOwner_whenNoRules_thenEmptyList() {
    when(repository.findByOwnerId("owner")).thenReturn(List.of());
    ResponseEntity<List<GetLintRule>> res = service.getAllByOwner("owner");
    assertEquals(200, res.getStatusCodeValue());
    assertNotNull(res.getBody());
    assertTrue(res.getBody().isEmpty());
  }

  @Test
  void givenGetAllByOwner_whenRepositoryThrows_thenInternalServerError() {
    when(repository.findByOwnerId("owner")).thenThrow(new RuntimeException("fail"));
    ResponseEntity<List<GetLintRule>> res = service.getAllByOwner("owner");
    assertEquals(500, res.getStatusCodeValue());
  }

  @Test
  void givenConvertToLintRule_whenListHasItems_thenReturnConverted() {
    Lint a = new Lint("owner", "r1", "v", true);
    Lint b = new Lint("owner", "r2", "v2", false);
    var converted = service.convertToLintRule(List.of(a, b));
    assertEquals(2, converted.size());
    assertEquals("r1", converted.get(0).name());
    assertEquals("r2", converted.get(1).name());
  }
}
