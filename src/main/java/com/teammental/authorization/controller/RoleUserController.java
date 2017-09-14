package com.teammental.authorization.controller;

import com.teammental.authorization.dto.RoleUserDto;
import com.teammental.authorization.exception.RoleUserException;
import com.teammental.authorization.service.RoleUserService;
import java.net.URI;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by okan on 6.09.2017.
 */

@RestController
public class RoleUserController {

  @Autowired
  RoleUserService roleUserService;

  private static final Logger LOGGER = LoggerFactory.getLogger(RoleUserController.class);

  public static final String AN_ERROR_OCCURED = "Bir Hata oluştu";

  public static final String REQUEST_MAPPING_GET_USERS_OF_ROLE = "roles/{id}/users";
  public static final String REQUEST_MAPPING_ADD_USERS_TO_ROLE = "roles/users";

  /**
   * Role ait kullanıcları getirir.
   *
   * @param id rolün id sidir.
   * @return hata mesajları veya List<RoleUsertDto>roleUserDtos</RoleUsertDto>
   */

  @GetMapping(value = REQUEST_MAPPING_GET_USERS_OF_ROLE)
  public ResponseEntity roleUserList(@PathVariable Integer id) {

    try {
      List<RoleUserDto> roleUserDtos = roleUserService.getAllUserByRole(id);
      return new ResponseEntity<List<RoleUserDto>>(roleUserDtos, HttpStatus.OK);
    } catch (RoleUserException e) {
      LOGGER.error("", e);
      if (e.getCode() == 0) {
        return new ResponseEntity<String>(e.getLabel(), HttpStatus.NOT_FOUND);
      }
    }
    return new ResponseEntity<String>(AN_ERROR_OCCURED, HttpStatus.INTERNAL_SERVER_ERROR);

  }


  //  @PostMapping(value = REQUEST_MAPPING_ADD_USERS_TO_ROLE)
  //  @ResponseBody
  //  public ResponseEntity addUsersToRole(@RequestBody RoleUserDto roleUserDtoList,
  //                                       HttpRequest httpRequest) throws Exception {
  //    LOGGER.info("post başladı.");
  //    return new ResponseEntity<String>("başarılı", HttpStatus.OK);

  //    try {
  //      int id;
  //      id = roleUserService.save(roleUserDtoList);
  //      if (id > 0) {
  //        URI location = new URI("/roles/" + id + "/users");
  //        return ResponseEntity.created(location).body("");
  //      }
  //    } catch (RoleUserException e) {
  //      LOGGER.error("", e);
  //      if (e.getCode() == 1) {
  //        return new ResponseEntity<String>(e.getLabel(), HttpStatus.NOT_FOUND);
  //      } else if (e.getCode() == 2) {
  //        return new ResponseEntity<String>(e.getLabel(), HttpStatus.NOT_FOUND);
  //      }
  //    }
  //    return new ResponseEntity<String>(AN_ERROR_OCCURED, HttpStatus.INTERNAL_SERVER_ERROR);
  //  }

  /**
   * Role user eklerken önce silen sonra liste halinde RoleUser tablosuna ekler.
   *
   * @param roleUserDtoList RoleUserDto listesi alır.
   * @param request         HttpRequest objesi alır.
   * @return Hata mesajları veya sonuç döner.
   * @throws Exception RoleUserException fırlatır.
   */

  @PostMapping(value = {REQUEST_MAPPING_ADD_USERS_TO_ROLE})
  @ResponseBody
  public ResponseEntity roleAddPost(@RequestBody List<RoleUserDto> roleUserDtoList,
                                    HttpServletRequest request) throws Exception {
    LOGGER.info("********* Post başladı.");
    LOGGER.info(request.getMethod());

    try {
      int id;

      id = roleUserService.save(roleUserDtoList);
      if (id > 0) {
        URI location = new URI("/roles/" + id + "/users");
        return ResponseEntity.created(location).body("");
      }
    } catch (RoleUserException e) {
      LOGGER.error("", e);
      if (e.getCode() == 1) {
        return new ResponseEntity<String>(e.getLabel(), HttpStatus.NOT_FOUND);
      } else if (e.getCode() == 2) {
        return new ResponseEntity<String>(e.getLabel(), HttpStatus.NOT_FOUND);
      }
    }
    return new ResponseEntity<String>(AN_ERROR_OCCURED, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}


