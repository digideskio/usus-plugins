// Generated source.
package org.projectusus.matchers;

public class UsusMatchers {

  public static <T> org.hamcrest.Matcher<java.util.Set<? extends T>> setOf(T... items) {
    return org.projectusus.matchers.IsSetOfMatcher.setOf(items);
  }

  public static <T> org.hamcrest.Matcher<java.util.Set<? extends T>> emptySet() {
    return org.projectusus.matchers.IsSetOfMatcher.<T>emptySet();
  }

  public static <T> org.hamcrest.Matcher<java.util.Set<? extends T>> setOf(T... items) {
    return org.projectusus.matchers.IsSetOfMatcher.<T>setOf(items);
  }

}
