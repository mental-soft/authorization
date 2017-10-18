package com.teammental.authorization.config.EntityGenerator;

import com.teammental.authorization.entity.Role;
import com.teammental.authorization.entity.RoleUser;
import com.teammental.mebuilder.GenericBuilder;
import com.teammental.memapper.util.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoleGenerator {

  /**
   * Generaretes a random list of roles
   *
   * @return list of roles
   */

  public static List<Role> prepareRandomListofRoles() {

    List<Role> roles = prepareRandomListofRoles(10);
    return roles;
  }

  /**
   * Generates a random list of roles with roleUsers.
   *
   * @return list of roles
   */

  public static List<Role> prepareRandomListofRoles(int size) {

    List<Role> roles = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      Role role = prepareRandomRole();
      roles.add(role);
    }
    return roles;
  }

  /**
   * Generates a random role with roleUsers
   *
   * @return role
   */

  public static Role prepareRandomRole() {

    Random random = new Random();
    Integer id = random.nextInt();
    String name = StringUtil.generateRandomString(5);
    String key = StringUtil.generateRandomString(5);

    Role role = GenericBuilder.of(Role::new)
        .with(Role::setId, id)
        .with(Role::setName, name)
        .with(Role::setKey, key)
        .build();

    List<RoleUser> roleUsers = RoleUserGenerator.prepareRandomListofRoleUser(role, 10);
    role.setRoleUsers(roleUsers);

    return role;

  }
}
