package org.sonar.com.csc.java;

import java.util.List;

import org.sonar.com.csc.java.checks.HardCodingCheck;
import org.sonar.plugins.java.api.JavaCheck;

import com.google.common.collect.ImmutableList;
/**
 * @author mmalik25
 * 
 */
public final class RulesList {

  private RulesList() {
  }

  public static List<Class> getChecks() {
    return ImmutableList.<Class>builder().addAll(getJavaChecks()).addAll(getJavaTestChecks()).build();
  }

  public static List<Class<? extends JavaCheck>> getJavaChecks() {
    return ImmutableList.<Class<? extends JavaCheck>>builder()
      .add(HardCodingCheck.class)
      .build();
  }

  public static List<Class<? extends JavaCheck>> getJavaTestChecks() {
    return ImmutableList.<Class<? extends JavaCheck>>builder()
      .build();
  }
}
