package com.example.myproject.controller;


import com.example.myproject.service.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final JwtTokenProvider tokenProvider;

    public AuthController(JwtTokenProvider jwtTokenProvider){
        this.tokenProvider = jwtTokenProvider;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> loginRequest){
        String username = loginRequest.get("username");

        String token = tokenProvider.createToken(username,"ROLE_USER");

        Map<String,String> response = new HashMap<>();
        response.put("token",token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/protected")
    public ResponseEntity<?> protectedResource(){
        return ResponseEntity.ok("인증 성공! 정상적으로 보호된 데이터 접근성공");
    }
}
