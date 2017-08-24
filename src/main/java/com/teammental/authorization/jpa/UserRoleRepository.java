package com.teammental.authorization.jpa;

import com.teammental.authorization.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by okan on 29.07.2017.
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Integer> {
      //
}
