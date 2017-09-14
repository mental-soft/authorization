package com.teammental.authorization.jpa;

import com.teammental.authorization.entity.Role;
import com.teammental.authorization.entity.RoleUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by okan on 29.07.2017.
 */
@Repository
public interface UserRoleRepository extends JpaRepository<RoleUser, Integer> {
  List<Role> findByRoleId(Integer roleId);

  Integer countByRoleId(Integer roleId);
}
