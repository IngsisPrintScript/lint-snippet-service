package com.ingsis.lintSnippetService.linting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LintingRepository extends JpaRepository<Lint, UUID> {
    Lint findByName(String name);

    List<Lint> findByOwnerIdAndActive(String ownerId, boolean active);
}
