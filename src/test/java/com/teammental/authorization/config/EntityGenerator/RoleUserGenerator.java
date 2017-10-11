package com.teammental.authorization.config.EntityGenerator;

import com.teammental.authorization.entity.Role;
import com.teammental.authorization.entity.RoleUser;
import com.teammental.mebuilder.GenericBuilder;
import com.teammental.memapper.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoleUserGenerator {

        /**
         * Generates list of roleusers
         * @param role role
         * @param size wanted count of districts
         * @return list of districts
         */

    public static List<RoleUser> prepareRandomListofRoleUser(Role role, int size) {
        Random random = new Random();
        List<RoleUser> roleUsers =  new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Integer id = random.nextInt();
            RoleUser roleUser = GenericBuilder.of(RoleUser::new)
                    .with(RoleUser::setId,id)
                    .with(RoleUser::setRole,role)
                    .build();
            roleUsers.add(roleUser);

        }
        return roleUsers;
    }

    /**
     * Generates a random list of roleusers
     * @param size wanted size of list
     * @return list of roleusers
     */

    public static List<RoleUser> prepareRandomListofRoleUser(int size) {
        Role role = RoleGenerator.prepareRandomRole();
        return prepareRandomListofRoleUser(role, size);
    }
}
