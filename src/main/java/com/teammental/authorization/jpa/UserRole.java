package com.teammental.authorization.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by okan on 29.07.2017.
 */
@Repository
public interface UserRole extends JpaRepository<UserRole,Integer> {

}
