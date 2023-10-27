package org.devcourse.resumeme.global.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.domain.user.UserCommonInfo;
import org.devcourse.resumeme.global.advice.exception.CustomException;
import org.devcourse.resumeme.global.auth.model.OAuth2TempInfo;
import org.devcourse.resumeme.global.auth.userInfo.GoogleOAuth2UserInfo;
import org.devcourse.resumeme.global.auth.userInfo.KakaoOAuth2UserInfo;
import org.devcourse.resumeme.global.auth.userInfo.OAuth2UserInfo;
import org.devcourse.resumeme.repository.MenteeRepository;
import org.devcourse.resumeme.repository.MentorRepository;
import org.devcourse.resumeme.repository.OAuth2InfoRedisRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.devcourse.resumeme.domain.user.Provider.GOOGLE;
import static org.devcourse.resumeme.domain.user.Provider.KAKAO;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2CustomUserService extends DefaultOAuth2UserService {

    private final MentorRepository mentorRepository;

    private final MenteeRepository menteeRepository;

    private final OAuth2InfoRedisRepository oAuth2TempInfoRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = null;

        if (provider.equals(GOOGLE.getDescription())) {
            userInfo = new GoogleOAuth2UserInfo(oAuth2User.getAttributes());
        }

        if (provider.equals(KAKAO.getDescription())) {
            userInfo = new KakaoOAuth2UserInfo(oAuth2User.getAttributes());
        }

        if (userInfo == null) {
            throw new CustomException("NOT_SUPPORTED_SOCIAL", "지원하지 않는 소셜로그인 입니다.");
        }

        String email = userInfo.getEmail();

        Optional<Mentor> findMentor = mentorRepository.findByEmail(email);
        Optional<Mentee> findMentee = menteeRepository.findByEmail(email);

        if (findMentor.isEmpty() && findMentee.isEmpty()) {

            OAuth2TempInfo oAuth2TempInfo = new OAuth2TempInfo(userInfo.getId(), userInfo.getProvider(), userInfo.getNickname(), userInfo.getEmail(), userInfo.getImageUrl());
            String cacheKey = oAuth2TempInfoRepository.save(oAuth2TempInfo).getId();
            log.info("Redis Temporarily saved key : {}", cacheKey);

            throw new OAuth2AuthenticationException(new OAuth2Error("NOT_REGISTERED"), cacheKey);
        }

        UserCommonInfo userCommonInfo = findMentor.map(UserCommonInfo::of).orElseGet(() -> UserCommonInfo.of(findMentee.get()));

        return new OAuth2CustomUser(userCommonInfo, oAuth2User.getAttributes());
    }

}
