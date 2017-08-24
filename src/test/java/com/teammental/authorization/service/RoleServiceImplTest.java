package com.teammental.authorization.service;


import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.teammental.authorization.Application;

import com.teammental.authorization.dto.RoleDto;
import com.teammental.authorization.entity.Role;
import com.teammental.authorization.jpa.RoleRepository;
import com.teammental.mebuilder.GenericBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)

/**
 * Created by okan on 5.08.2017.
 */
public class RoleServiceImplTest {
  @InjectMocks
  private RoleService roleService = new RoleServiceImpl();
  @MockBean
  private RoleRepository roleRepository;

  //@Before
  //public void init() {
    //reset(roleRepository);
   // MockitoAnnotations.initMocks(this);
  //}

  @Test
  public void getAll() throws Exception {
    List<Role>  roles = new ArrayList<>();
    Role role = new Role();
    role.setId(1);
    role.setKey("as");
    role.setName("sadf");
    roles.add(role);

    when(roleRepository.findAll()).thenReturn(roles);

    List<RoleDto> roleDtos = roleService.getAll();
    assertEquals(1, roleDtos.size());
    assertEquals(1,(int)roleDtos.get(0).getId());
  }

  //region saveOrUpdate()
  @Test
  public void saveOrUpdate_WhenDtoEmpty_ShouldReturnException() {
    try {
      roleService.saveOrUpdate(null);
      Assert.fail();
    } catch (Exception e) {
      assertEquals(RoleServiceImpl.PARAMETERS_MUST_BE_NOT_NULL, e.getMessage());
    }
  }

  //endregion

  //region saveOrUpdate()

  @Test
  public void saveOrUpdate_WhenDtoNameEmpty_ShouldReturnException() {
    try {
      roleService.saveOrUpdate(GenericBuilder.of(RoleDto::new).build());
      Assert.fail();
    } catch (Exception e) {
      assertEquals(RoleServiceImpl.ROLE_NAME_MUST_BE_NOT_NULL, e.getMessage());
    }
  }

  @Test
  public void saveOrUpdate_WhenDtoFull_ShouldReturnEntityId() {
    Role entity = new Role();

    entity.setKey("ROLE_CREATE");
    entity.setName("Rol Ekle");

   // given(roleRepository.save(any(Role.class))).willReturn(entity);

    try {
      RoleDto roleDto = GenericBuilder.of(RoleDto::new)

          .with(RoleDto::setKey,"ROLE_CREATE")
          .with(RoleDto::setName,"Rol Ekle")
          .build();

      int entityId = roleService.saveOrUpdate(roleDto);
      assertEquals(5, entityId);
    } catch (Exception e) {
      Assert.fail();
    }
  }




}