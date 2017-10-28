package com.teammental.authorization.controller;

import com.teammental.authorization.config.EntityGenerator.RoleUserGenerator;
import com.teammental.authorization.config.TestUtil;
import com.teammental.authorization.dto.RoleUserDto;
import com.teammental.authorization.entity.RoleUser;
import com.teammental.authorization.exception.RoleUserException;
import com.teammental.authorization.service.RoleUserService;
import com.teammental.authorization.service.RoleUserServiceImpl;
import com.teammental.meconfig.handler.rest.EntityNotFoundExceptionRestHandler;
import com.teammental.memapper.MeMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("PMD.TooManyStaticImports")
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(RoleUserController.class)
@Import(EntityNotFoundExceptionRestHandler.class)
public class RoleUserControllerTest {

  public static final String NOT_FOUND_MESSAGE = "Herhangi bir rol ve kullanıcı bulunamadı.";
  private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
  @Autowired
  MockMvc mockMvc;
  @MockBean
  RoleUserService roleUserService;

  //region GET_USERS_OF_ROLE

  @Test
  public void shouldReturnOkAndRoleUsers_whenRoleUserFound() throws Exception {

    final int roleUserSize = 2;

    List<RoleUser> expectedRoles2 = RoleUserGenerator.generateRandomListOfRoleUser(roleUserSize);

    Optional<List<RoleUserDto>> expectedDtosOptional2 = MeMapper.getMapperFromList(expectedRoles2)
        .mapToList(RoleUserDto.class);
    List<RoleUserDto> expectedDtos = expectedDtosOptional2.get();

    doReturn(expectedDtos).when(roleUserService).getAllUserByRole(anyInt());

    MvcResult mvcResult = mockMvc.perform(get("/roles/1/users"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$", hasSize(roleUserSize)))
        .andExpect(jsonPath("$[0].userId", is(expectedDtos.get(0).getUserId())))
        .andReturn();
    LOGGER.info(mvcResult.getResponse().getContentAsString());
    verify(roleUserService, times(1)).getAllUserByRole(anyInt());
  }

  @Test
  public void shouldReturn404_whenNoRoleUserFound() throws Exception {

    doThrow(new RoleUserException(0, NOT_FOUND_MESSAGE))
        .when(roleUserService).getAllUserByRole(anyInt());

    mockMvc.perform(get("/roles/1/users"))
        .andExpect(status().isNotFound());

    verify(roleUserService, times(1)).getAllUserByRole(anyInt());
  }

  //endregion GET_USERS_OF_ROLE

  //region ADD_USERS_TO_ROLE

  @Test
  public void insert_shouldReturn201AndLocation_whenFound() throws Exception {

    final List<RoleUserDto> roleUserDtoList = RoleUserGenerator.
        generateRandomRoleUserDtoList(10);
    final Integer expectedId = 1313;

    doReturn(expectedId).when(roleUserService).save(anyObject());

    mockMvc.perform(post(RoleUserController.REQUEST_MAPPING_ADD_USERS_TO_ROLE)
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(roleUserDtoList)))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location",
            "/roles/" + expectedId + "/users"));

    verify(roleUserService, times(1))
        .save(anyObject());
    verifyNoMoreInteractions(roleUserService);

  }

  @Test
  public void insert_shouldReturn400_whenValidationFails() throws Exception {

    List<RoleUserDto> roleUserDtoList = new ArrayList<>();
    when(roleUserService.save(anyObject())).thenThrow(new RoleUserException(2,
        RoleUserServiceImpl.LIST_OF_PARAMETERS_MUST_BE_NOT_NULL));

    mockMvc.perform(post(RoleUserController.REQUEST_MAPPING_ADD_USERS_TO_ROLE)
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(roleUserDtoList)))
        .andExpect(status().isBadRequest());

    verify(roleUserService, times(1))
        .save(anyObject());
    verifyNoMoreInteractions(roleUserService);

  }

  @Test
  public void insert_shouldReturn500_whenInsertException() throws Exception {

    final List<RoleUserDto> roleUserDtoList = RoleUserGenerator.
        generateRandomRoleUserDtoList(10);

    when(roleUserService.save(anyObject())).thenThrow(new RuntimeException());

    mockMvc.perform(post(RoleUserController.REQUEST_MAPPING_ADD_USERS_TO_ROLE)
        .contentType(TestUtil.APPLICATION_JSON_UTF8)
        .content(TestUtil.convertObjectToJsonBytes(roleUserDtoList)))
        .andExpect(status().isInternalServerError());

    verify(roleUserService, times(1))
        .save(anyObject());
    verifyNoMoreInteractions(roleUserService);

  }

  //endregion ADD_USERS_TO_ROLE
}
