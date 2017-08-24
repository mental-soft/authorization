package com.teammental.authorization.controller;

import com.teammental.authorization.dto.RoleDto;
import com.teammental.authorization.service.RoleService;

//import java.util.ArrayList;
import java.net.URI;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by okan on 5.08.2017.
 */
@RestController
public class RoleController {

  @Autowired
  RoleService roleService;

  private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

  public static final String KEY_REQUIRED = "*Anahtar alanı zorunludur.";
  public static final String NAME_REQUIRED = "*Ad alanı zorunludur.";
  public static final String ALREADY_EXIST_KEY = "Bu anahtar kelime ile bir rol zaten var.";
  public static final String SAVE_ERROR = "Kaydetme işlemi sırasında bir hata oluştu.";
  public static final String ROLE_NOT_FOUND = "Herhangi bir rol bulunamadı.";

  /**
   * Rolleri listeleme işlemi
   *
   * @return roleDtos json nesnesi veya hata mesajı
   */
  @RequestMapping(value = "roles", method = RequestMethod.GET, produces = "application/json")
   public ResponseEntity roleList() {

    List<RoleDto> roleDtos = roleService.getAll();
    if (roleDtos.size() > 0) {
      return new ResponseEntity<List<RoleDto>>(roleDtos , HttpStatus.OK);
    } else {
      return new ResponseEntity<String>(ROLE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

  }

  /**
   * Role ekleme veya güncelleme işlemi.
   *
   * @param roleDto Eklenmesi veya güncellenmesi istenen RoleDto nesnesi
   * @return mesajlar
   */

  @RequestMapping(value = "/roles", produces = "application/json",
      consumes = "application/json")
  @PostMapping
  @ResponseBody
  public ResponseEntity<String> rolePost(@RequestBody RoleDto roleDto) throws Exception {

    LOGGER.info("********* Post başladı.");


    if (roleDto != null) {

      try {

        if ((roleDto.getKey() == null || roleDto.getKey().isEmpty()) &&
            (roleDto.getName() == null || roleDto.getName().isEmpty())) {
          return new ResponseEntity<String>(KEY_REQUIRED + NAME_REQUIRED, HttpStatus.BAD_REQUEST);
        } else if (roleDto.getKey() == null || roleDto.getKey().isEmpty()) {
          return new ResponseEntity<String>( KEY_REQUIRED, HttpStatus.BAD_REQUEST);
        } else if (roleDto.getName() == null || roleDto.getName().isEmpty()) {
          return new ResponseEntity<String>(NAME_REQUIRED, HttpStatus.BAD_REQUEST);
        }
        int id;
        id = roleService.saveOrUpdate(roleDto);
        if (id > 0) {

          URI location = new URI("/roles/" + id);
          return ResponseEntity.created(location).body("");
        }
      } catch (DataIntegrityViolationException e) {
        LOGGER.info("********* DataIntegrityViolationException. " + e.getMessage());
        return new ResponseEntity<String>( ALREADY_EXIST_KEY, HttpStatus.CONFLICT);
      } catch (Exception e) {
        LOGGER.info("********* Hataya düştü. " + e.getMessage());
        return new ResponseEntity<String>(SAVE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    return new ResponseEntity<String>(SAVE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

  }

}
