package com.itrex.java.lab.entity.dto;

import com.itrex.java.lab.entity.Role;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private int id;
    private String name;
    private String password;
    private Role role;
    private String email;
    private List<CertificateDTO> certificates;
}
