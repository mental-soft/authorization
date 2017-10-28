package com.teammental.authorization.dto;

/**
 * Created by okan on 3.09.2017.
 */
public class RoleUserDto {

  private Integer id;
  private Integer roleId;
  private Integer userId;

  private RoleDto roleDto;

  public RoleDto getRoleDto() {

    return roleDto;
  }

  public void setRoleDto(RoleDto roleDto) {

    this.roleDto = roleDto;
  }

  public Integer getId() {

    return id;
  }

  public void setId(Integer id) {

    this.id = id;
  }

  public Integer getRoleId() {

    return roleId;
  }

  public void setRoleId(Integer roleId) {

    this.roleId = roleId;
  }

  public Integer getUserId() {

    return userId;
  }

  public void setUserId(Integer userId) {

    this.userId = userId;
  }
}
