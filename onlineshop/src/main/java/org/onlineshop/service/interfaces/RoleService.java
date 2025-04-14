package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Role;
import org.onlineshop.model.enums.RoleName;

import java.util.Optional;

public interface RoleService {
    Optional<Role> findRoleByRoleName(RoleName roleName);
}
