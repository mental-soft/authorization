package com.teammental.authorization.controller;

import com.teammental.authorization.config.EntityGenerator.RoleGenerator;
import com.teammental.authorization.config.TestUtil;
import com.teammental.authorization.dto.RoleDto;
import com.teammental.authorization.entity.Role;
import com.teammental.authorization.exception.RoleException;
import com.teammental.authorization.service.RoleService;
import com.teammental.authorization.service.RoleServiceImpl;
import com.teammental.meconfig.handler.rest.EntityNotFoundExceptionRestHandler;
import com.teammental.memapper.MeMapper;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("PMD.TooManyStaticImports")
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(RoleController.class)
@Import(EntityNotFoundExceptionRestHandler.class)

public class RoleControllerTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
  @MockBean
  private RoleService roleService;
  @Autowired
  private MockMvc mockMvc;

  // region getAll

  @Test
  public void getAll_shouldReturnOkAndRoles_whenRolesFound() throws Exception {

    final int roleSize = 2;
    List<Role> expectedRoles = RoleGenerator.generateRandomListofRoles(roleSize);

    Optional<List<RoleDto>> expectedDtosOptional = MeMapper.getMapperFromList(expectedRoles)
        .mapToList(RoleDto.class);
    List<RoleDto> expectedDtos = expectedDtosOptional.get();

    doReturn(expectedDtos).when(roleService).getAll();

    mockMvc.perform(get(RoleController.REQUEST_MAPPING_ROLES))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(roleSize)))
        .andExpect(jsonPath("$[0].id", is(expectedDtos.get(0).getId())))
        .andExpect(jsonPath("$[0].name", is(expectedDtos.get(0).getName())))
        .andExpect(jsonPath("$[1].id", is(expectedDtos.get(1).getId())))
        .andExpect(jsonPath("$[1].name", is(expectedDtos.get(1).getName())));

    verify(roleService, times(1)).getAll();

  }

  @Test
  public void getAll_shouldReturn404_whenNotFoundAnyRole() throws Exception {

    final int roleSize = 0;
    List<Role> expectedRoles = RoleGenerator.generateRandomListofRoles(roleSize);

    Optional<List<RoleDto>> expectedDtosOptional = MeMapper.getMapperFromList(expectedRoles)
        .mapToList(RoleDto.class);
    List<RoleDto> expectedDtos = expectedDtosOptional.get();

    doReturn(expectedDtos).when(roleService).getAll();

    mockMvc.perform(get(RoleController.REQUEST_MAPPING_ROLES))
        .andExpect(status().isNotFound());

    verify(roleService, times(1)).getAll();
  }

  // endregion getAll

  //region getById

  @Test
  public void getById_shouldReturnOkAndRole_whenFound() throws Exception {

    final RoleDto roleDto = RoleGenerator.generateRandomRoleDto();
    final Integer id = 11;

    doReturn(roleDto).when(roleService).getById(id);

    mockMvc.perform(get("/roles/{id}", id.toString()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.id", is(roleDto.getId())))
        .andExpect(jsonPath("$.name", is(roleDto.getName())))
        .andExpect(jsonPath("$.key", is(roleDto.getKey())));

    verify(roleService, times(1))
        .getById(id);
    verifyNoMoreInteractions(roleService);
  }

  @Test
  public void getById_shouldReturn404_whenNotFoundAnyRole() throws Exception {

    final Integer id = 666;

    when(roleService.getById(id))
        .thenThrow(new Exception());

    mockMvc.perform(get("/roles/{id}", id.toString()))
        .andExpect(status().isNotFound());

    verify(roleService, times(1))
        .getById(id);
    verifyNoMoreInteractions(roleService);

  }

  //endregion getById

  //region insert

  @Test
  public void insert_shouldReturn201AndLocation_whenInsert() throws Exception {

    final int expectedId = 313;

    doReturn(expectedId).when(roleService).saveOrUpdate(anyObject());
//    when(roleService.saveOrUpdate(anyObject()))
//        .thenReturn(expectedId);
    final Role role = new RoleGenerator().generateRandomRole();
    final RoleDto roleDto = (RoleDto) MeMapper.getMapperFrom(role)
        .mapTo(RoleDto.class).get();

    mockMvc.perform(post(RoleController.REQUEST_MAPPING_ROLES)
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(roleDto)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location",
            RoleController.REQUEST_MAPPING_ROLES + "/" + expectedId));

    verify(roleService, times(1))
        .saveOrUpdate(anyObject());

  }

  @Test
  public void insert_shouldReturn400_whenValidationFails() throws Exception {

    RoleDto roleDto = new RoleDto();

    mockMvc.perform(post(RoleController.REQUEST_MAPPING_ROLES)
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(roleDto)))
        .andExpect(status().isBadRequest());

    verify(roleService, times(0))
        .saveOrUpdate(anyObject());

  }

  @Test
  public void insert_shouldReturn500_whenInsertException() throws Exception {

    RoleDto roleDto = RoleGenerator.generateRandomRoleDto();
    when(roleService.saveOrUpdate(anyObject()))
        .thenThrow(new Exception());

    mockMvc.perform(post(RoleController.REQUEST_MAPPING_ROLES)
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(roleDto)))
        .andExpect(status().isInternalServerError());

    verify(roleService, times(1))
        .saveOrUpdate(anyObject());
    verifyNoMoreInteractions(roleService);

  }

  //endregion insert

  //region update

  @Test
  public void update_shouldReturn200_whenSuccess() throws Exception {

    final RoleDto roleDto = RoleGenerator.generateRandomRoleDto();
    final Integer id = 11;
    roleDto.setId(id);
    doReturn(id).when(roleService).saveOrUpdate(anyObject());

    mockMvc.perform(put("/roles/{id}", id.toString())
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(roleDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(roleDto.getId())))
        .andExpect(jsonPath("$.name", is(roleDto.getName())))
        .andExpect(jsonPath("$.key", is(roleDto.getKey())));

    verify(roleService, times(1))
        .saveOrUpdate(anyObject());

  }

  @Test
  public void update_shouldReturn409_whenAlreadyExist() throws Exception {

    final RoleDto roleDto = RoleGenerator.generateRandomRoleDto();

    when(roleService.saveOrUpdate(anyObject()))
        .thenThrow(new DataIntegrityViolationException(RoleController.ALREADY_EXIST_KEY));

    mockMvc.perform(put("/roles/{id}", roleDto.getId())
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(roleDto)))
        .andExpect(status().isConflict());

    verify(roleService, times(1))
        .saveOrUpdate(anyObject());

    verifyNoMoreInteractions(roleService);
  }

  @Test
  public void update_shouldReturn400_whenValidationFails() throws Exception {

    RoleDto roleDto = new RoleDto();
    final Integer id = 11;

    mockMvc.perform(put("/roles/{id}", id)
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(roleDto)))
        .andExpect(status().isBadRequest());

    verify(roleService, times(0))
        .saveOrUpdate(anyObject());

  }

  @Test
  public void update_shouldReturn500_whenUpdateException() throws Exception {

    RoleDto roleDto = RoleGenerator.generateRandomRoleDto();
    Random random = new Random();
    when(roleService.saveOrUpdate(anyObject()))
        .thenThrow(new Exception());

    mockMvc.perform(put("/roles/{id}",
        random.nextInt(Integer.MAX_VALUE - 1) + 1)
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(roleDto)))
        .andExpect(status().isInternalServerError());

    verify(roleService, times(1))
        .saveOrUpdate(anyObject());
    verifyNoMoreInteractions(roleService);

  }

  //endregion update

  //region delete

  @Test
  public void delete_should204_whenSuccess() throws Exception {

    Random random = new Random();
    Integer id = random.nextInt(Integer.MAX_VALUE - 1) + 1;
    Mockito.doNothing().when(roleService).deleteById(anyInt());

    mockMvc.perform(delete("/roles/{id}", id.toString())
        .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    verify(roleService, times(1))
        .deleteById(id);

  }

  @Test
  public void delete_shouldReturn404_whenNotFound() throws Exception {

    Random random = new Random();
    Integer id = random.nextInt(Integer.MAX_VALUE - 1) + 1;

    Mockito.doThrow(new RoleException(1, RoleServiceImpl.NOT_FOUND_MESSAGE))
        .when(roleService).deleteById(id);

    mockMvc.perform(delete("/roles/{id}", id.toString())
        .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNotFound());

    verify(roleService, times(1))
        .deleteById(id);

  }

  @Test
  public void delete_shouldReturn403_whenUserFound() throws Exception {

    Random random = new Random();
    Integer id = random.nextInt(Integer.MAX_VALUE - 1) + 1;

    Mockito.doThrow(new RoleException(0, RoleServiceImpl.ROLE_SHOULD_NOT_HAVE_USERROLE))
        .when(roleService).deleteById(id);

    mockMvc.perform(delete("/roles/{id}", id.toString())
        .contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isForbidden());

    verify(roleService, times(1))
        .deleteById(id);

  }

  //endregion delete

}
