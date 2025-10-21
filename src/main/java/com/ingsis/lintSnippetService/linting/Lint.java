package com.ingsis.lintSnippetService.linting;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

@Entity
public class Lint {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank private String ownerId;
  @NotBlank private String name;
  @NotBlank private String defaultValue;
  private boolean active;

  public Lint(String ownerId, String name, String defaultValue, boolean active) {
    this.ownerId = ownerId;
    this.name = name;
    this.defaultValue = defaultValue;
    this.active = active;
  }

  public Lint() {}

  public UUID getId() {
    return id;
  }

  public String getOwnerId() {
    return ownerId;
  }

  public String getName() {
    return name;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public boolean isActive() {
    return active;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public void setActive(boolean active) {
    this.active = active;
  }
}
