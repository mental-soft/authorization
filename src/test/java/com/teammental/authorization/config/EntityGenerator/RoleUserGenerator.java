package com.teammental.authorization.config.EntityGenerator;

import com.teammental.authorization.dto.RoleUserDto;
import com.teammental.authorization.entity.Role;
import com.teammental.authorization.entity.RoleUser;
import com.teammental.mebuilder.GenericBuilder;
import com.teammental.memapper.MeMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RoleUserGenerator {

  /**
   * Generates list of roleusers
   *
   * @param role role
   * @param size wanted count of districts
   * @return list of districts
   */

  public static List<RoleUser> generateRandomListOfRoleUser(Role role, int size) {

    Random random = new Random();
    List<RoleUser> roleUsers = new ArrayList<>();
    for (int i = 0; i < size; i++) {
      Integer id = random.nextInt(Integer.MAX_VALUE - 1) + 1;
      RoleUser roleUser = GenericBuilder.of(RoleUser::new)
          .with(RoleUser::setId, id)
          .with(RoleUser::setRole, role)
          .build();
      roleUsers.add(roleUser);

    }
    return roleUsers;
  }

  /**
   * Generates a random list of roleusers
   *
   * @param size wanted size of list
   * @return list of roleusers
   */

  public static List<RoleUser> generateRandomListOfRoleUser(int size) {

    Role role = RoleGenerator.generateRandomRole();
    return generateRandomListOfRoleUser(role, size);
  }

  /**
   * Generates a random RoleUser object.
   *
   * @param clientId custom clientId
   * @return RoleUser object
   */
  public static RoleUser generateRandomRoleUser(final Integer clientId) {

    Random random = new Random();
    final Integer id = random.nextInt();
    RoleUser roleUser = GenericBuilder.of(RoleUser::new)
        .with(RoleUser::setId, id)
        .with(RoleUser::setRole, RoleGenerator.generateRandomRole())
        .build();
    return roleUser;
  }

  /**
   * Generates a random RoleUser object.
   *
   * @return RoleUser object
   */
  public static RoleUser generateRandomRoleUser() {

    Random random = new Random();
    int clientId = random.nextInt(Integer.MAX_VALUE - 1) + 1;
    return generateRandomRoleUser(clientId);
  }

  /**
   * Generates a random RoleUserDto.
   *
   * @return RoleUserDto
   */
  public static RoleUserDto generateRandomRoleUserDto() {

    RoleUser roleUser = generateRandomRoleUser();
    RoleUserDto roleUserDto = (RoleUserDto) MeMapper.getMapperFrom(roleUser)
        .mapTo(RoleUserDto.class).get();
    return roleUserDto;
  }

  public static List<RoleUserDto> generateRandomRoleUserDtoList(int size) {

    List<RoleUser> roleUserList = generateRandomListOfRoleUser(size);
    Optional<List<RoleUserDto>> targets = MeMapper.getMapperFromList(roleUserList)
        .mapToList(RoleUser.class);
    return targets.get();
  }
}
