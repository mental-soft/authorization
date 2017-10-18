package com.teammental.authorization.service;

import com.teammental.authorization.dto.RoleDto;
import com.teammental.authorization.entity.Role;
import com.teammental.authorization.exception.RoleException;
import com.teammental.authorization.jpa.RoleRepository;
import com.teammental.mebuilder.GenericBuilder;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * Created by okan on 5.08.2017.
 */
@RunWith(SpringRunner.class)
@SuppressWarnings("PMD.TooManyStaticImports")
public class RoleServiceImplTest {

  @InjectMocks
  private RoleService roleService = new RoleServiceImpl();
  @MockBean(name = "roleRepository")
  private RoleRepository roleRepository;
  @MockBean
  private RoleUserService roleUserService;

  @Before
  public void init() {

    reset(roleRepository);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void getAll() throws Exception {

    List<Role> roles = new ArrayList<>();

    Role role = GenericBuilder.of(Role::new)
        .with(Role::setId, 1)
        .with(Role::setKey, "as")
        .with(Role::setName, "sadf")
        .build();

    roles.add(role);

    when(roleRepository.findAll()).thenReturn(roles);

    List<RoleDto> roleDtos = roleService.getAll();
    //assertEquals(1, roleDtos.size());
    assertEquals(1, (int) roleDtos.get(0).getId());
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
    entity.setId(5);
    entity.setKey("ROLE_CREATE");
    entity.setName("Rol Ekle");

    given(roleRepository.save(any(Role.class))).willReturn(entity);

    try {
      RoleDto roleDto = GenericBuilder.of(RoleDto::new)
          .with(RoleDto::setId, 5)
          .with(RoleDto::setKey, "ROLE_CREATE")
          .with(RoleDto::setName, "Rol Ekle")
          .build();

      int entityId = roleService.saveOrUpdate(roleDto);
      assertEquals(5, entityId);
    } catch (Exception e) {
      Assert.fail();
    }
  }

  @Test
  public void getById_WhenEmpty_ShouldReturnException() {

    given(roleRepository.findOne(anyInt())).willReturn(null);

    try {
      roleService.getById(anyInt());
      Assert.fail();
    } catch (Exception e) {
      assertEquals(RoleServiceImpl.NOT_FOUND_MESSAGE, e.getMessage());
    }
  }

  @Test
  public void getById_WhenFull_ShouldReturnInfo() {

    Role role = GenericBuilder.of(Role::new)
        .with(Role::setId, 1)
        .with(Role::setKey, "RoleKey")
        .with(Role::setName, "RoleName")
        .build();

    given(roleRepository.findOne(anyInt())).willReturn(role);

    try {
      RoleDto dto = roleService.getById(anyInt());

      int id = dto.getId();
      assertEquals(1, id);
    } catch (Exception e) {
      Assert.fail();
    }
  }

  @Test
  public void deleteById_WhenEmpty_ShouldReturnException() {

    doThrow(EmptyResultDataAccessException.class).when(roleRepository).delete(anyInt());
    given(roleUserService.existUserByRole(anyInt())).willReturn(false);

    try {
      roleService.deleteById(anyInt());
      Assert.fail();
    } catch (Exception e) {
      assertEquals(RoleException.class, e.getClass());
    }
  }

  @Test
  public void deleteById_WhenExistUser_ShouldReturnException() {

    given(roleUserService.existUserByRole(anyInt())).willReturn(true);
    try {
      roleService.deleteById(anyInt());
      Assert.fail();
    } catch (RoleException e) {
      assertEquals(RoleServiceImpl.ROLE_SHOULD_NOT_HAVE_USERROLE, e.getLabel());
    }
  }

  @Test
  public void deleteById_WhenFull_ShouldDeleteSuccess() {

    given(roleRepository.exists(anyInt())).willReturn(true);
    try {
      roleService.deleteById(anyInt());
    } catch (Exception e) {
      Assert.fail();
    }
  }

}

