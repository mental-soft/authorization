package com.teammental.authorization;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FirstController.class)
public class ApplicationTest {

  @Autowired
  private MockMvc mvc;

  /**
   * Index sayfasının testi.
   *
   * @throws Exception hata
   */
  @Test
  public void getIndex() throws Exception {

    mvc.perform(MockMvcRequestBuilders.get("/")
        .accept(MediaType.TEXT_HTML))
        .andExpect(status().isOk());
  }

}
