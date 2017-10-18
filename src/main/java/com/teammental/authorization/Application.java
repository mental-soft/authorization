package com.teammental.authorization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by okan on 1.07.2017.
 */

@SpringBootApplication
public class Application extends WebMvcConfigurerAdapter {

  /**
   * static void main.
   *
   * @param args string dizisi
   */

  public static void main(String[] args) {

    SpringApplication.run(Application.class, args);
  }
}