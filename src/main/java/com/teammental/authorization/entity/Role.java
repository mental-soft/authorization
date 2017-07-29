package com.teammental.authorization.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
  private int id;

  @Column(name = "key")
  private String key;

  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "Role", cascade = CascadeType.ALL)
  private List<UserRole> userRoles;

  public int getId() {
    return id;
  }

  public void setId(int id) {
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

  public List<UserRole> getUserRoles() {
    return userRoles;
  }

  public void setUserRoles(List<UserRole> userRoles) {
    this.userRoles = userRoles;
  }

}
