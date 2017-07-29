package com.teammental.authorization.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by okan on 29.07.2017.
 */
@Entity
@Table(name = "USER_ROLE")
public class UserRole {

  @Column(name = "USER_ID", columnDefinition = "numeric")
  private int userId;

  @Column(name = "ROLE_ID", columnDefinition = "integer")
  private int roleId;

  @ManyToOne
  @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
  private Role role;

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getRoleId() {
    return roleId;
  }

  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

}
