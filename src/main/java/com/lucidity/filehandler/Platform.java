package com.lucidity.filehandler;

// Enum for Operating System
public enum Platform {
  WINDOWS("Windows"),
  MAC("MAC"),
  LINUX("Linux");

  private final String operatingSystem;

  /**
   * Constructor to initialise the name of the Operating System
   *
   * @param operatingSystem name of the Operating System
   */
  Platform(String operatingSystem) {
    this.operatingSystem = operatingSystem;
  }

  /**
   * Provides the name of the operating system
   *
   * @return name of the operating system
   */
  public String getOperatingSystem() {
    return operatingSystem;
  }
}
