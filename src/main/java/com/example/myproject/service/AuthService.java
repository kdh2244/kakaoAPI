package com.example.myproject.service;

import com.example.myproject.dto.KakaoDTO;
import com.example.myproject.entity.User;
import com.example.myproject.repository.UserRepository;
import com.example.myproject.util.KakaoUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private KakaoUtil kakaoUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  JwtTokenProvider jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;




    public User oAuthLogin(String accessCode, HttpServletResponse httpServletResponse){
        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode);
        System.out.println("oAuthToken : "+oAuthToken);
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);

        String email = kakaoProfile.getKakao_account().getEmail();

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createNewUser(kakaoProfile));

        String token = jwtUtil.createToken(user.getEmail(),user.getRole().toString());
        httpServletResponse.setHeader("Authorization",token);

        return user;
    }

    private User createNewUser(KakaoDTO.KakaoProfile kakaoProfile){
        User newUser = AuthConverter.toUser(
                kakaoProfile.getKakao_account().getEmail(),
                kakaoProfile.getKakao_account().getProfile().getNickname(),
                null,
                passwordEncoder
        );
        return userRepository.save(newUser);
    }
}
