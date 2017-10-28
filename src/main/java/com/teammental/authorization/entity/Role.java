package com.teammental.authorization.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Created by okan on 29.07.2017.
 */
@Entity
@Table(name = "ROLE")
public class Role {

  @Id
  @Column(name = "id", columnDefinition = "integer")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "key")
  private String key;

  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<RoleUser> roleUsers;

  public Integer getId() {

    return id;
  }

  public void setId(Integer id) {

    this.id = id;
  }

  public String getKey() {

    return key;
  }

  public void setKey(String key) {

    this.key = key;
  }

  public String getName() {

    return name;
  }

  public void setName(String name) {

    this.name = name;
  }

  public List<RoleUser> getRoleUsers() {

    return roleUsers;
  }

  public void setRoleUsers(List<RoleUser> roleUsers) {

    this.roleUsers = roleUsers;
  }

}
