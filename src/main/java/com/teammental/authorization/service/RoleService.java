package com.teammental.authorization.service;

import com.teammental.authorization.dto.RoleDto;

import java.util.List;

/**
 * Created by okan on 5.08.2017.
 */
public interface RoleService {

  List<RoleDto> getAll();

  RoleDto getById(Integer roleId) throws Exception;

  void deleteById(Integer roleId) throws Exception;

  int saveOrUpdate(RoleDto dto) throws Exception;
}
