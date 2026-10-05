package com.example.myproject.controller;

import com.example.myproject.entity.User;
import com.example.myproject.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RestController
@RequestMapping("/auth/login/kakao")
public class LoginController {



    @Autowired
    AuthService authService;

    @GetMapping
    public ResponseEntity<?> kakaoLogin(@RequestParam String code,
                                                                  HttpServletResponse httpServletResponse){
        System.out.println("요청 들어옴");
        User user = authService.oAuthLogin(code,httpServletResponse);
        return ResponseEntity.ok(user);
    }
}
