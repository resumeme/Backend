package org.devcourse.resumeme.global.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devcourse.resumeme.domain.mentee.Mentee;
import org.devcourse.resumeme.domain.mentor.Mentor;
import org.devcourse.resumeme.domain.user.Provider;
import org.devcourse.resumeme.global.auth.model.UserCommonInfo;
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

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2CustomUserService extends DefaultOAuth2UserService {

    private final MentorRepository mentorRepository;

    private final MenteeRepository menteeRepository;

    private final OAuth2InfoRedisRepository oAuth2TempInfoRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String providerName = userRequest.getClientRegistration().getRegistrationId();

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo userInfo = Provider.of(providerName).convert(oAuth2User.getAttributes());

        String email = userInfo.getEmail();

        Optional<Mentor> findMentor = mentorRepository.findByEmail(email);
        Optional<Mentee> findMentee = menteeRepository.findByEmail(email);

        if (isNewUser(findMentor, findMentee)) {
            String cacheKey = oAuth2TempInfoRepository.save(userInfo.toOAuth2TempInfo()).getId();
            log.info("Redis Temporarily saved key : {}", cacheKey);

            return new OAuth2CustomUser(null, Map.of("key", cacheKey));
        }
        return new OAuth2CustomUser(getUserCommonInfo(findMentor, findMentee), oAuth2User.getAttributes());
    }

    private boolean isNewUser(Optional<Mentor> findMentor, Optional<Mentee> findMentee) {
        return findMentor.isEmpty() && findMentee.isEmpty();
    }

    private UserCommonInfo getUserCommonInfo(Optional<Mentor> findMentor, Optional<Mentee> findMentee) {
        return findMentor.map(UserCommonInfo::of)
                .orElseGet(() -> UserCommonInfo.of(findMentee.get()));
    }

}
