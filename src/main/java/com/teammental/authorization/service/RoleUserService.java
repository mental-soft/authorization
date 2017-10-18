package com.teammental.authorization.service;

import com.teammental.authorization.dto.RoleUserDto;
import com.teammental.authorization.exception.RoleUserException;
import java.util.List;

/**
 * Created by okan on 3.09.2017.
 */
public interface RoleUserService {

  List<RoleUserDto> getAllUserByRole(Integer roleId) throws RoleUserException;

  Boolean existUserByRole(Integer roleId);

  void deleteByRoleId(Integer roleId) throws Exception;

  int save(List<RoleUserDto> roleUserDtoList) throws RoleUserException;

}
