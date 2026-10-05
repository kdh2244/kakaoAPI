package com.example.myproject.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private String email;
    //private String role;
    //private String password;
    private String name;
}
