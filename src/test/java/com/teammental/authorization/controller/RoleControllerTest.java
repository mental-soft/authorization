package com.teammental.authorization.controller;

import com.teammental.authorization.config.EntityGenerator.RoleGenerator;
import com.teammental.authorization.config.TestUtil;
import com.teammental.authorization.dto.RoleDto;
import com.teammental.authorization.entity.Role;
import com.teammental.authorization.service.RoleService;
import com.teammental.meconfig.handler.rest.EntityNotFoundExceptionRestHandler;
import com.teammental.memapper.MeMapper;
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

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SuppressWarnings("PMD.TooManyStaticImports")
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(RoleController.class)
@Import(EntityNotFoundExceptionRestHandler.class)

public class RoleControllerTest {

    @MockBean
    private RoleService roleService;

    @Autowired
    private MockMvc mockMvc;

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Test
    public void shouldReturnOkAndRoles_whenRolesFound() throws Exception {
        final int roleSize = 2;
        List<Role> expectedRoles = RoleGenerator.prepareRandomListofRoles(roleSize);

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
    public void shouldReturn404_whenNoRoleFound() throws Exception {
        final int roleSize = 0;
        List<Role> expectedRoles = RoleGenerator.prepareRandomListofRoles(roleSize);

        Optional<List<RoleDto>> expectedDtosOptional = MeMapper.getMapperFromList(expectedRoles)
                .mapToList(RoleDto.class);
        List<RoleDto> expectedDtos = expectedDtosOptional.get();

        doReturn(expectedDtos).when(roleService).getAll();

        mockMvc.perform(get(RoleController.REQUEST_MAPPING_ROLES))
                .andExpect(status().isNotFound());

        verify(roleService, times(1)).getAll();
    }

}
