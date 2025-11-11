package com.ingsis.lintSnippetService.linting;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class LintingRepositoryReflectionTest {

  @Test
  void repositoryHasExpectedMethods() {
    Method[] methods = LintingRepository.class.getDeclaredMethods();
    boolean hasFindByOwnerIdAndActive = false;
    boolean hasFindByNameAndOwnerId = false;
    boolean hasFindByOwnerIdAndId = false;
    boolean hasFindByOwnerId = false;
    for (Method m : methods) {
      String n = m.getName();
      if (n.equals("findByOwnerIdAndActive")) hasFindByOwnerIdAndActive = true;
      if (n.equals("findByNameAndOwnerId")) hasFindByNameAndOwnerId = true;
      if (n.equals("findByOwnerIdAndId")) hasFindByOwnerIdAndId = true;
      if (n.equals("findByOwnerId")) hasFindByOwnerId = true;
    }
    assertTrue(hasFindByOwnerIdAndActive);
    assertTrue(hasFindByNameAndOwnerId);
    assertTrue(hasFindByOwnerIdAndId);
    assertTrue(hasFindByOwnerId);
  }
}
