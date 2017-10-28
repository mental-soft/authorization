package com.teammental.authorization.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by okan on 29.07.2017.
 */
@Entity
@Table(name = "USER_ROLE")
public class RoleUser {

  @Id
  @Column(name = "id", columnDefinition = "integer")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "user_id", columnDefinition = "numeric")
  private Integer userId;

  @Column(name = "role_id", columnDefinition = "integer", insertable = false, updatable = false)
  private Integer roleId;

  @ManyToOne
  @JoinColumn(name = "role_id", referencedColumnName = "id")
  @JsonIgnore
  private Role role;

  public Integer getId() {

    return id;
  }

  public void setId(Integer id) {

    this.id = id;
  }

  public Integer getUserId() {

    return userId;
  }

  public void setUserId(Integer userId) {

    this.userId = userId;
  }

  public Integer getRoleId() {

    return roleId;
  }

  public void setRoleId(Integer roleId) {

    this.roleId = roleId;
  }

  public Role getRole() {

    return role;
  }

  public void setRole(Role role) {

    this.role = role;
  }

}
