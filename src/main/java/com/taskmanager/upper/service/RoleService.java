package com.taskmanager.upper.service;

import com.taskmanager.upper.entity.AuthRoles;
import com.taskmanager.upper.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    public AuthRoles findRolesById(Long id){
        return roleRepository.getById(id);
    }
}
