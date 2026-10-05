package com.example.myproject.util;

import ch.qos.logback.core.status.ErrorStatus;
import com.example.myproject.dto.KakaoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

@Slf4j
@Component
public class KakaoUtil {

    @Value("${kakao.client.id}")
    private String client;

    @Value("${kakao.redirect}")
    private String redirect;

    public KakaoDTO.OAuthToken requestToken(String accessCode){
        System.out.println("client : "+client);
        System.out.println("redirect : "+redirect);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type",
                "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("grant_type","authorization_code");
        params.add("client_id",client);
        params.add("redirect_uri",redirect);
        params.add("code",accessCode);

        HttpEntity<MultiValueMap<String,String>> kakaoTokenRequest
                = new HttpEntity<>(params,headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();

        KakaoDTO.OAuthToken oAuthToken = null;

        try{
            oAuthToken = objectMapper.readValue(response.getBody(),KakaoDTO.OAuthToken.class);
            log.info("oAuthToken : "+oAuthToken.getAccess_token());
        }catch(Exception e){
            //throw new AuthHandler(ErrorStatus._PARSING_ERROR);
        }
        return oAuthToken;

    }

    public KakaoDTO.KakaoProfile requestProfile(KakaoDTO.OAuthToken oAuthToken){
        RestTemplate restTemplate2 = new RestTemplate();
        HttpHeaders httpHeaders2 = new HttpHeaders();

        httpHeaders2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        httpHeaders2.add("Authorization","Bearer "+
                oAuthToken.getAccess_token());

        HttpEntity<MultiValueMap<String,String>> kakaoProfileRequest =
                new HttpEntity<>(httpHeaders2);

        ResponseEntity<String> response2 = restTemplate2.postForEntity(
                "https://kapi.kakao.com/v2/user/me",
                kakaoProfileRequest,
                String.class);

        System.out.println("tokenResponse : "+response2);


        KakaoDTO.KakaoProfile kakaoProfile = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try{
           kakaoProfile = objectMapper.readValue(response2.getBody(),KakaoDTO.KakaoProfile.class);
        }catch (JsonProcessingException e){
            System.out.println(Arrays.toString(e.getStackTrace()));

        }
        System.out.println("kakaoProfile : "+kakaoProfile);
        return kakaoProfile;
    }
}
