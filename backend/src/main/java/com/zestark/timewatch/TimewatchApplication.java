package com.zestark.timewatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Timewatch Application - Time tracking and analysis application.
 *
 * <p>This is the main entry point for the Spring Boot application.
 * Manages task timing, tracking, and time usage analytics.
 */
@SpringBootApplication
public class TimewatchApplication {

  public static void main(String[] args) {
    SpringApplication.run(TimewatchApplication.class, args);
  }
}
