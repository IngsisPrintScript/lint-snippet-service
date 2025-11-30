package com.ingsis.lintSnippetService;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class LintingServiceApplicationTest {

  @Test
  void mainMethodExists() throws NoSuchMethodException {
    Method m = LintingServiceApplication.class.getMethod("main", String[].class);
    assertNotNull(m);
  }
}
