package com.teammental.authorization.controller;

//import com.sun.org.apache.xpath.internal.operations.Bool;

import com.teammental.authorization.dto.RoleDto;
import com.teammental.authorization.exception.RoleException;
import com.teammental.authorization.service.RoleService;
import java.net.URI;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  public static final String REQUEST_MAPPING_ROLE_ONE = "/roles/{id}";
  public static final String REQUEST_MAPPING_ROLES = "roles";


  /**
   * Rolleri listeleme işlemi.
   *
   * @return roleDtos json nesnesi veya hata mesajı
   */

  @GetMapping(value = REQUEST_MAPPING_ROLES)
  public ResponseEntity roleList() {

    List<RoleDto> roleDtos = roleService.getAll();
    if (roleDtos.size() > 0) {
      return new ResponseEntity<List<RoleDto>>(roleDtos, HttpStatus.OK);
    } else {
      return new ResponseEntity<String>(ROLE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

  }

  /**
   * Role ekleme işlemi.
   *
   * @param roleDto Eklenmesi istenen RoleDto nesnesi
   * @return mesajlar
   */

  @PostMapping(value = {REQUEST_MAPPING_ROLES})
  @ResponseBody
  public ResponseEntity roleAddPost(@RequestBody RoleDto roleDto,
                                    HttpServletRequest request) throws Exception {
    LOGGER.info("********* Post başladı.");
    LOGGER.info(request.getMethod());

    if (roleDto != null) {

      try {

        ResponseEntity responseEntity = getResponseEntity(roleDto);
        if (responseEntity != null) {
          return responseEntity;
        }
        int id;
        id = roleService.saveOrUpdate(roleDto);
        if (id > 0) {
          URI location = new URI("/roles/" + id);
          return ResponseEntity.created(location).body("");
        }

      } catch (DataIntegrityViolationException e) {
        LOGGER.info("********* DataIntegrityViolationException. " + e.getMessage());
        return new ResponseEntity<String>(ALREADY_EXIST_KEY, HttpStatus.CONFLICT);
      } catch (Exception e) {
        LOGGER.info("********* Hataya düştü. " + e.getMessage());
        return new ResponseEntity<String>(SAVE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    return new ResponseEntity<String>(SAVE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

  }

  private ResponseEntity getResponseEntity(@RequestBody RoleDto roleDto) {
    if (roleDto.getKey().isEmpty()
        && roleDto.getName().isEmpty()) {
      return new ResponseEntity<String>(KEY_REQUIRED + NAME_REQUIRED, HttpStatus.BAD_REQUEST);
    } else if (roleDto.getKey().isEmpty()) {
      return new ResponseEntity<String>(KEY_REQUIRED, HttpStatus.BAD_REQUEST);
    } else if (roleDto.getName().isEmpty()) {
      return new ResponseEntity<String>(NAME_REQUIRED, HttpStatus.BAD_REQUEST);
    }
    return null;
  }

  /**
   * Role güncelleme işlemi.
   *
   * @param roleDto güncellenmesi istenen RoleDto nesnesi
   * @return mesajlar
   */


  @PutMapping(value = {REQUEST_MAPPING_ROLE_ONE})
  @ResponseBody
  public ResponseEntity roleUpdatePut(@RequestBody RoleDto roleDto, @PathVariable int id,
                                      HttpServletRequest request) throws Exception {
    LOGGER.info("********* Put başladı.");
    LOGGER.info(request.getMethod());

    if (roleDto != null) {

      try {

        ResponseEntity responseEntity = getResponseEntity(roleDto);
        if (responseEntity != null) {
          return responseEntity;
        }
        if (id > 0) {
          roleDto.setId(id);
          roleService.saveOrUpdate(roleDto);
          return new ResponseEntity<RoleDto>(roleDto, HttpStatus.OK);
        }
      } catch (DataIntegrityViolationException e) {
        LOGGER.info("********* DataIntegrityViolationException. " + e.getMessage());
        return new ResponseEntity<String>(ALREADY_EXIST_KEY, HttpStatus.CONFLICT);
      } catch (Exception e) {
        LOGGER.info("********* Hataya düştü. " + e.getMessage());
        return new ResponseEntity<String>(SAVE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    return new ResponseEntity<String>(SAVE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

  }

  /**
   * Id ye göre bir adet rol getirme işlemi.
   *
   * @return roleDto json nesnesi veya hata mesajı
   */
  @GetMapping(value = REQUEST_MAPPING_ROLE_ONE,
      produces = "application/json")
  public ResponseEntity roleDetail(@PathVariable(value = "id") Integer id) {
    RoleDto roleDto;

    try {
      roleDto = roleService.getById(id);
      return new ResponseEntity<RoleDto>(roleDto, HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.info("********* Hataya düştü. " + e.getMessage());
      return new ResponseEntity<String>(ROLE_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Id ye göre role tablosunda rol siler.
   *
   * @param id role id si alır.
   * @return mesajlar.
   */
  @DeleteMapping(value = REQUEST_MAPPING_ROLE_ONE)
  public ResponseEntity roleDelete(@PathVariable(value = "id") Integer id) {

    try {
      roleService.deleteById(id);
      return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    } catch (RoleException e) {
      LOGGER.error("", e);
      if (e.getCode() == 0) {
        return new ResponseEntity<String>(e.getLabel(), HttpStatus.NOT_FOUND);
      } else if (e.getCode() == 1) {
        return new ResponseEntity<String>(e.getLabel(), HttpStatus.NOT_FOUND);
      }
    }
    return new ResponseEntity<String>(SAVE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
  }

}




