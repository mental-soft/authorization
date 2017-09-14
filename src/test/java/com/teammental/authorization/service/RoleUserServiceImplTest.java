package com.teammental.authorization.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.reset;

import com.teammental.authorization.dto.RoleUserDto;
import com.teammental.authorization.entity.RoleUser;
import com.teammental.authorization.exception.RoleUserException;
import com.teammental.authorization.jpa.RoleUserRepository;
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
import org.springframework.test.context.junit4.SpringRunner;



/**
 * Created by okan on 6.09.2017.
 */
@RunWith(SpringRunner.class)
@SuppressWarnings("PMD.TooManyStaticImports")
public class RoleUserServiceImplTest {
  @InjectMocks
  private RoleUserService roleUserService = new RoleUserServiceImpl();
  @MockBean(name = "roleUserRepository")
  private RoleUserRepository roleUserRepository;

  @Before
  public void init() {
    reset(roleUserRepository);
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void getByRoleId_WhenFull_ShouldReturnInfo() throws RoleUserException {

    List<RoleUser> roleUsers = new ArrayList<>();

    RoleUser roleUser = GenericBuilder.of(RoleUser::new)
        .with(RoleUser::setId, 1)
        .with(RoleUser::setUserId, 1)
        .with(RoleUser::setRoleId, 1)
        .build();

    roleUsers.add(roleUser);

    given(roleUserRepository.findByRoleId(anyInt())).willReturn(roleUsers);
    List<RoleUserDto> roleUserDtos = roleUserService.getAllUserByRole(anyInt());

    assertEquals(1, (int) roleUserDtos.get(0).getId());

  }

  @Test
  public void getByRoleId_WhenEmpty_ShouldReturnException() {
    given(roleUserRepository.findByRoleId(anyInt())).willReturn(null);

    try {
      roleUserService.getAllUserByRole(anyInt());
      Assert.fail();
    } catch (RoleUserException e) {
      assertEquals(RoleUserServiceImpl.NOT_FOUND_MESSAGE, e.getLabel());
    }
  }

  @Test
  public void deleteById_WhenFull_ShouldDeleteSuccess() {
    given(roleUserRepository.countByRoleId(anyInt())).willReturn(1);
    try {
      roleUserService.deleteByRoleId(anyInt());
    } catch (Exception e) {
      Assert.fail();
    }

  }

  @Test
  public void save_WhenDtoListEmpty_ShouldReturnException() {
    try {
      roleUserService.save(null);
      Assert.fail();
    } catch (RoleUserException e) {
      assertEquals(RoleUserServiceImpl.PARAMETERS_MUST_BE_NOT_NULL, e.getLabel());
    }
  }

  @Test
  public void save_WhenDtoListItemEmpty_ShouldReturnException() {
    RoleUserDto roleUserDto = GenericBuilder.of(RoleUserDto::new)
        .with(RoleUserDto::setRoleId, 5)
        .build();
    RoleUserDto roleUserDto2 = GenericBuilder.of(RoleUserDto::new)
        .with(RoleUserDto::setUserId, 5)
        .build();
    List<RoleUserDto> roleUserDtos = new ArrayList<>();
    roleUserDtos.add(roleUserDto);
    roleUserDtos.add(roleUserDto2);
    try {

      roleUserService.save(roleUserDtos);
      Assert.fail();
    } catch (RoleUserException e) {
      assertEquals(RoleUserServiceImpl.LIST_OF_PARAMETERS_MUST_BE_NOT_NULL, e.getLabel());
    }
  }

  @Test
  public void save_WhenDtoListFull_ShouldReturnEntityRoleId() {
    RoleUser roleUser = GenericBuilder.of(RoleUser::new)
        .with(RoleUser::setRoleId, 10)
        .with(RoleUser::setUserId, 5)
        .build();
    RoleUser roleUser1 = GenericBuilder.of(RoleUser::new)
        .with(RoleUser::setRoleId, 10)
        .with(RoleUser::setUserId, 6)
        .build();
    List<RoleUser> roleUserList = new ArrayList<>();
    roleUserList.add(roleUser);
    roleUserList.add(roleUser1);
    given(roleUserRepository.save(anyListOf(RoleUser.class))).willReturn(roleUserList);

    try {
      RoleUserDto roleUserDto = GenericBuilder.of(RoleUserDto::new)
          .with(RoleUserDto::setRoleId, 10)
          .with(RoleUserDto::setUserId, 5)
          .build();
      RoleUserDto roleUserDto1 = GenericBuilder.of(RoleUserDto::new)
          .with(RoleUserDto::setRoleId, 10)
          .with(RoleUserDto::setUserId, 6)
          .build();
      List<RoleUserDto> roleUserDtoList = new ArrayList<>();
      roleUserDtoList.add(roleUserDto);
      roleUserDtoList.add(roleUserDto1);

      int entityRoleId = roleUserService.save(roleUserDtoList);
      assertEquals(10,entityRoleId);
    } catch (Exception e) {
      Assert.fail();
    }

  }

  @Test
  public void save_WhenDtoListFullWithDelete_ShouldReturnEntityRoleId() {
    RoleUser roleUser = GenericBuilder.of(RoleUser::new)
        .with(RoleUser::setRoleId, 10)
        .with(RoleUser::setUserId, 5)
        .build();
    RoleUser roleUser1 = GenericBuilder.of(RoleUser::new)
        .with(RoleUser::setRoleId, 10)
        .with(RoleUser::setUserId, 6)
        .build();
    List<RoleUser> roleUserList = new ArrayList<>();
    roleUserList.add(roleUser);
    roleUserList.add(roleUser1);
    given(roleUserRepository.save(anyListOf(RoleUser.class))).willReturn(roleUserList);
    given(roleUserRepository.countByRoleId(anyInt())).willReturn(1);

    try {
      RoleUserDto roleUserDto = GenericBuilder.of(RoleUserDto::new)
          .with(RoleUserDto::setRoleId, 10)
          .with(RoleUserDto::setUserId, 5)
          .build();
      RoleUserDto roleUserDto1 = GenericBuilder.of(RoleUserDto::new)
          .with(RoleUserDto::setRoleId, 10)
          .with(RoleUserDto::setUserId, 6)
          .build();
      List<RoleUserDto> roleUserDtoList = new ArrayList<>();
      roleUserDtoList.add(roleUserDto);
      roleUserDtoList.add(roleUserDto1);

      int entityRoleId = roleUserService.save(roleUserDtoList);
      assertEquals(10,entityRoleId);
    } catch (Exception e) {
      Assert.fail();
    }

  }


}
