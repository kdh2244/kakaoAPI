package com.example.myproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class User {

    @Id
    private String email;
    private String role;
    private String password;
    private String name;

}
