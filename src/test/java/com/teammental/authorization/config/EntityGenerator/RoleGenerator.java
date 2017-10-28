package com.teammental.authorization.config.EntityGenerator;

import com.teammental.authorization.dto.RoleDto;
import com.teammental.authorization.entity.Role;
import com.teammental.authorization.entity.RoleUser;
import com.teammental.mebuilder.GenericBuilder;
import com.teammental.memapper.MeMapper;
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

  public static List<Role> generateRandomListofRoles() {

    List<Role> roles = generateRandomListofRoles(10);
    return roles;
  }

  /**
   * Generates a random list of roles with roleUsers.
   *
   * @return list of roles
   */

  public static List<Role> generateRandomListofRoles(int size) {

    List<Role> roles = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      Role role = generateRandomRole();
      roles.add(role);
    }
    return roles;
  }

  /**
   * Generates a random role with roleUsers
   *
   * @return role
   */

  public static Role generateRandomRole() {

    Random random = new Random();
    Integer id = random.nextInt(Integer.MAX_VALUE - 1)  + 1;
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

  /**
   * Generates a random RoleDto object.
   * @return created object
   */
  public static RoleDto generateRandomRoleDto() {
    Role role = generateRandomRole();
    RoleDto roleDto = (RoleDto) MeMapper.getMapperFrom(role)
        .mapTo(RoleDto.class).get();
    return roleDto;
  }
}
