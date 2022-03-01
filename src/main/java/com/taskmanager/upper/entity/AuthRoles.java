package com.taskmanager.upper.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name="roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthRoles implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String role;
    private String description;

    @Override
    public String getAuthority() {
        return role;
    }

    public String toString(){
        return this.role;
    }
    public String toStringDes(){
        return this.description;
    }
}
