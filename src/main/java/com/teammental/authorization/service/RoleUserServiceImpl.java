package com.teammental.authorization.service;

import com.teammental.authorization.dto.RoleUserDto;
import com.teammental.authorization.entity.RoleUser;
import com.teammental.authorization.exception.RoleUserException;
import com.teammental.authorization.jpa.RoleRepository;
import com.teammental.authorization.jpa.RoleUserRepository;
import com.teammental.memapper.MeMapper;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by okan on 3.09.2017.
 */
@Service
public class RoleUserServiceImpl implements RoleUserService {

  //region Messages
  public static final String NOT_FOUND_MESSAGE = "Herhangi bir rol ve kullanıcı bulunamadı.";
  public static final String PARAMETERS_MUST_BE_NOT_NULL = "Parametre girilmesi gerekmektedir.";
  public static final String LIST_OF_PARAMETERS_MUST_BE_NOT_NULL =
      "Liste elemanları girilmesi gerekmektedir.";
  private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RoleUserServiceImpl.class);
  //endregion
  @Autowired
  RoleRepository roleRepository;

  @Autowired
  RoleUserRepository roleUserRepository;

  @Override
  public List<RoleUserDto> getAllUserByRole(Integer roleId) throws RoleUserException {

    List<RoleUser> roleUserList = roleUserRepository.findByRoleId(roleId);
    if (roleUserList == null) {
      throw new RoleUserException(0, NOT_FOUND_MESSAGE);
    }
    Optional<List<RoleUserDto>> targets = MeMapper.getMapperFromList(roleUserList)
        .mapToList(RoleUserDto.class);
    return targets.get();
  }

  @Override
  public Boolean existUserByRole(Integer roleId) {

    return roleUserRepository.countByRoleId(roleId) > 0 ? true : false;
  }

  @Override
  @Transactional
  public void deleteByRoleId(Integer roleId) {

    if (existUserByRole(roleId)) {
      LOGGER.info("servis delete: " + roleId);

      roleUserRepository.deleteByRoleId(roleId);
    }
  }

  @Override
  @Transactional
  public int save(List<RoleUserDto> roleUserDtoList) throws RoleUserException {

    if (roleUserDtoList == null) {
      throw new RoleUserException(1, PARAMETERS_MUST_BE_NOT_NULL);
    }

    //roleUserDtoList içindeki veriler kontrol edilecek.
    if (nullCheckListItem(roleUserDtoList)) {
      throw new RoleUserException(2, LIST_OF_PARAMETERS_MUST_BE_NOT_NULL);
    }

    Optional<List<RoleUser>> target = MeMapper.getMapperFromList(roleUserDtoList)
        .mapToList(RoleUser.class);
    List<RoleUser> roleUsersEntity;
    roleUsersEntity = target.get();
    if (roleUsersEntity != null && roleUsersEntity.size() > 0) {
      LOGGER.info("servis roleId: " + roleUsersEntity.get(0).getRoleId());
      deleteByRoleId(roleUsersEntity.get(0).getRoleId());
      //      LOGGER.info("servis roleId: " + roleUsersEntity.get(0).getRoleId());
      //      LOGGER.info("servis roleId: " + roleUsersEntity.get(1).getRoleId());
      //      LOGGER.info("servis roleId: " + roleUsersEntity.get(2).getRoleId());
      //      LOGGER.info("servis roleId: " + roleUsersEntity.get(2).getRole().getName());
      roleUsersEntity = roleUserRepository.save(roleUsersEntity);
      //      for (RoleUser entity : roleUsersEntity) {
      //        roleUserRepository.save(entity);
      //      }

      return roleUsersEntity.get(0).getRoleId();
    } else {
      return 0;
    }

  }

  private boolean nullCheckListItem(List<RoleUserDto> roleUserDtoList) {

    List<RoleUserDto> roleUserDtos;
    roleUserDtos = roleUserDtoList.stream()
        .filter(r -> r.getRoleId() == null || r.getUserId() == null
            || r.getRoleId() == 0 || r.getUserId() == 0)
        .collect(Collectors.toList());

    if (roleUserDtos.size() > 0) {
      return true;
    } else {
      return false;
    }

  }

}
