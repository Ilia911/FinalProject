package com.itrex.java.lab.entity.dto;

import com.itrex.java.lab.entity.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    private int id;
    private String name;
    private Role role;

    public UserDTO(int id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
}
