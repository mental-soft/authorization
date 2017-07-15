package com.teammental.authorization;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by okan on 8.07.2017.
 */
@Controller
public class FirstController {

  /**
   * Projenin deneme index controller.
   * @return index sayfası
   */
  @RequestMapping({"/","/index"})
  public String getIndex() {
    return "index";
  }
}
