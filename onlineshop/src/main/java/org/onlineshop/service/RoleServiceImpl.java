package org.onlineshop.service;

import org.onlineshop.model.entity.Role;
import org.onlineshop.model.enums.RoleName;
import org.onlineshop.repository.RoleRepository;
import org.onlineshop.service.interfaces.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> findRoleByRoleName(RoleName roleName) {
        return this.roleRepository.findByRoleName(roleName);
    }
}
