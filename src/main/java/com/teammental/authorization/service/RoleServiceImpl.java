package com.teammental.authorization.service;

import com.teammental.authorization.dto.RoleDto;
import com.teammental.authorization.entity.Role;
import com.teammental.authorization.exception.RoleException;
import com.teammental.authorization.jpa.RoleRepository;
import com.teammental.memapper.MeMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by okan on 5.08.2017.
 */
@Service
public class RoleServiceImpl implements RoleService {

  //region Messages
  public static final String NOT_FOUND_MESSAGE = "Role kaydı bulunamadı.";
  public static final String PARAMETERS_MUST_BE_NOT_NULL = "Parametre girilmesi gerekmektedir.";
  public static final String ROLE_NAME_MUST_BE_NOT_NULL = "Role Adı girilmesi gerekmektedir.";
  public static final String ROLE_SHOULD_NOT_HAVE_USERROLE = "Role ait kullanıcılar bulunmaktadır.";
  //endregion

  //private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);
  @Autowired
  RoleRepository roleRepository;

  @Autowired
  RoleUserService roleUserService;

  @Override
  public List<RoleDto> getAll() {

    List<Role> list = roleRepository.findAll();
    Optional<List<RoleDto>> targets = MeMapper.getMapperFromList(list).mapToList(RoleDto.class);
    return targets.get();

  }

  @Override
  public RoleDto getById(Integer roleId) throws Exception {

    Role entity = roleRepository.findOne(roleId);

    if (entity == null) {
      throw new Exception(NOT_FOUND_MESSAGE);
    }
    Optional<RoleDto> target = MeMapper.getMapperFrom(entity).mapTo(RoleDto.class);
    return target.get();
  }

  @Override
  public void deleteById(Integer roleId) throws RoleException {
    //userrole tablosunda role varsa kontrol yapılacak.
    if (roleUserService.existUserByRole(roleId)) {
      throw new RoleException(0, ROLE_SHOULD_NOT_HAVE_USERROLE);
    }
    if (!roleRepository.exists(roleId)) {
      throw new RoleException(1, NOT_FOUND_MESSAGE);
    }
    roleRepository.delete(roleId);
  }

  @Override
  public int saveOrUpdate(RoleDto dto) throws Exception {

    if (dto == null) {
      throw new Exception(PARAMETERS_MUST_BE_NOT_NULL);
    }

    if (dto.getName() == null || dto.getName().isEmpty()) {
      throw new Exception(ROLE_NAME_MUST_BE_NOT_NULL);
    }

    Optional<Role> target = MeMapper.getMapperFrom(dto).mapTo(Role.class);

    Role entity;
    entity = target.get();
    entity = roleRepository.save(entity);

    return entity.getId();
  }
}
