package com.taskmanager.upper.service;

import com.taskmanager.upper.entity.AuthRoles;
import com.taskmanager.upper.entity.AuthUsers;
import com.taskmanager.upper.repository.RoleRepository;
import com.taskmanager.upper.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.ArrayList;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {



    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AuthUsers user = userRepository.findByEmail(email);
        if(user!=null){
            return user;
        }else{
            throw new UsernameNotFoundException("USER NOT FOUND");
        }
    }

    public AuthUsers saveUser(AuthUsers user){
        AuthUsers checkUser = userRepository.findByEmail(user.getEmail());
        if(checkUser==null) {
            AuthRoles role = roleRepository.findByRole("ROLE_USER");
            if(role!=null){
                ArrayList<AuthRoles> roles = new ArrayList<>();
                roles.add(role);
                user.setRole(roles);
                return userRepository.save(user);
            }

        }
        return null;
    }

    public void  assignRole(AuthUsers user){
        userRepository.save(user);
    }

    public List<AuthUsers> getAllUsers(){
        return userRepository.findAll();
    }


    public AuthUsers findUser(Long id){
        AuthUsers user = userRepository.findById(id).orElse(null);
        return user;
    }

    public List<AuthRoles> getAllRoles(){
        return roleRepository.findAll();
    }

    public List<AuthUsers> getOnlyUsers(){
       List<AuthUsers>  users = userRepository.findAllByRole();
        return users;
    }
}
