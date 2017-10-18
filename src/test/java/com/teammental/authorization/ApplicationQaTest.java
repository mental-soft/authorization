package com.teammental.authorization;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@ActiveProfiles(value = {"qa", "prod"})
public class ApplicationQaTest {

  @Value(value = "${liquibase.drop-first}")
  String isDropFirst;

  /**
   * Liquibase qa ortamında drop yapmaması için test ediliyor.
   *
   * @throws Exception exception
   */
  @Test
  public void checkDropFirst() throws Exception {

    if (!isDropFirst.equals("${liquibase.drop-first}")) {
      assertFalse(Boolean.getBoolean(isDropFirst));
    }
  }

}
