package com.teammental.authorization;

import static org.junit.Assert.assertEquals;

import liquibase.integration.spring.SpringLiquibase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("qa")
public class ApplicationQaTest {

  @Autowired
  SpringLiquibase liquibase;

  /**
   * Liquibase dropfirst olmamasının testi.
   * @throws Exception hata
   */
  @Test
  public void checkDropFirst() throws Exception {
    assertEquals(false, liquibase.isDropFirst());
  }

}
