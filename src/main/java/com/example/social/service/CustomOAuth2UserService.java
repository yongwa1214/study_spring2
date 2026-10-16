package com.example.social.service;

import com.example.social.dto.*;
import com.example.social.entity.UserEntity;
import com.example.social.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;
        if(registrationId.equals("naver")){
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if(registrationId.equals("google")){
            oAuth2Response =  new GoogleResponse(oAuth2User.getAttributes());
        }
        else{
            return null;
        }
        // 추후 작성
        String username = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();

        UserEntity existData = userRepository.findByUsername(username);
        if(existData == null){
            UserEntity userEntity = UserEntity.builder()
                    .username(username)
                    .name(oAuth2Response.getName())
                    .email(oAuth2Response.getEmail())
                    .role("ROLE_USER")
                    .build();

            userRepository.save(userEntity);

            UserDTO userDTO = UserDTO.builder()
                    .username(username)
                    .name(oAuth2User.getName())
                    .role("ROLE_USER")
                    .build();
        }
        else {

        }


        return new CustomOAuth2User(userDTO);
    }
}
