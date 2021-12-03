package com.itrex.java.lab.entity.dto;

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
    private RoleDTO role;
    private String email;
    private List<CertificateDTO> certificates;
//
//    public static UserDTO convertIntoUser(User user) {
//        return UserDTO.builder()
//                .id(user.getId())
//                .name(user.getName())
//                .password("hidden")
//                .role(user.getRole())
//                .email(user.getEmail())
//                .certificates(user.getCertificates())
//                .build();
//    }
}
