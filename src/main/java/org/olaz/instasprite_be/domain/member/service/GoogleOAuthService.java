package org.olaz.instasprite_be.domain.member.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import org.olaz.instasprite_be.domain.member.dto.GoogleUserInfo;
import org.olaz.instasprite_be.domain.member.dto.JwtDto;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
import org.olaz.instasprite_be.domain.search.entity.SearchMember;
import org.olaz.instasprite_be.domain.search.repository.SearchMemberRepository;
import org.olaz.instasprite_be.global.error.exception.GoogleAuthFailException;
import org.olaz.instasprite_be.global.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOAuthService {

    private final MemberRepository memberRepository;
    private final SearchMemberRepository searchMemberRepository;
    private final JwtUtil jwtUtil;

    @Value("${google.client-id}")
    private String googleClientId;

    /**
     * Verify Google ID token and extract user information
     */
    public GoogleUserInfo verifyGoogleToken(String idToken) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken == null) {
                throw new GoogleAuthFailException();
            }

            Payload payload = googleIdToken.getPayload();
            
            return GoogleUserInfo.builder()
                    .googleId(payload.getSubject())
                    .email(payload.getEmail())
                    .name((String) payload.get("name"))
                    .pictureUrl((String) payload.get("picture"))
                    .emailVerified(payload.getEmailVerified())
                    .build();

        } catch (GeneralSecurityException | IOException e) {
            log.error("Error verifying Google token", e);
            throw new GoogleAuthFailException();
        }
    }

    /**
     * Login or register user with Google account
     */
    @Transactional
    public JwtDto loginWithGoogle(String idToken) {
        try {
            log.info("Starting Google login process");
            GoogleUserInfo googleUserInfo = verifyGoogleToken(idToken);
            log.info("Google token verified for user: {}", googleUserInfo.getEmail());

            boolean isFirstTime = !memberRepository.existsByGoogleId(googleUserInfo.getGoogleId());
            log.info("Is first time user: {}", isFirstTime);
            
            Member member = memberRepository.findByGoogleId(googleUserInfo.getGoogleId())
                    .orElseGet(() -> {
                        log.info("Creating new user for Google ID: {}", googleUserInfo.getGoogleId());
                        return createGoogleUser(googleUserInfo);
                    });

            log.info("Member found/created with ID: {}, Username: {}", member.getId(), member.getUsername());

            JwtDto jwtDto = jwtUtil.generateTokenDto(member);
            log.info("JWT tokens generated successfully");

            JwtDto result = JwtDto.builder()
                    .type(jwtDto.getType())
                    .accessToken(jwtDto.getAccessToken())
                    .refreshToken(jwtDto.getRefreshToken())
                    .name(jwtDto.getName())
                    .username(jwtDto.getUsername())
                    .email(jwtDto.getEmail())
                    .isFirstTime(isFirstTime)
                    .build();
            
            log.info("Google login process completed successfully for user: {}", result.getUsername());
            return result;
        } catch (Exception e) {
            log.error("Error in Google login process", e);
            throw e;
        }
    }

    /**
     * Create new user from Google account
     */
    private Member createGoogleUser(GoogleUserInfo googleUserInfo) {
        try {
            // Generate unique username from email
            String baseUsername = generateUsernameFromEmail(googleUserInfo.getEmail());
            String uniqueUsername = ensureUniqueUsername(baseUsername);

            Member member = Member.builder()
                    .username(uniqueUsername)
                    .name(googleUserInfo.getName())
                    .googleId(googleUserInfo.getGoogleId())
                    .email(googleUserInfo.getEmail())
                    .build();

            log.info("Saving member to database: {}", member.getUsername());
            Member savedMember = memberRepository.save(member);
            log.info("Member saved successfully with ID: {}", savedMember.getId());

            // Create search member entry
            try {
                SearchMember searchMember = new SearchMember(savedMember);
                searchMemberRepository.save(searchMember);
                log.info("SearchMember created successfully for user: {}", savedMember.getUsername());
            } catch (Exception e) {
                log.error("Failed to create SearchMember for user: {}", savedMember.getUsername(), e);
                // Don't rethrow - the main member is already saved
            }

            log.info("Created new Google user: {}", savedMember.getUsername());
            return savedMember;
        } catch (Exception e) {
            log.error("Failed to create Google user: {}", googleUserInfo.getEmail(), e);
            throw e;
        }
    }

    /**
     * Generate username from email
     */
    private String generateUsernameFromEmail(String email) {
        String username = email.split("@")[0];
        // Remove special characters and ensure it meets username requirements
        username = username.replaceAll("[^a-zA-Z0-9]", "");
        
        // Ensure minimum length of 4
        if (username.length() < 4) {
            username = username + "user";
        }
        
        // Ensure maximum length of 12
        if (username.length() > 12) {
            username = username.substring(0, 12);
        }
        
        return username.toLowerCase();
    }

    /**
     * Ensure username is unique by appending numbers if necessary
     */
    private String ensureUniqueUsername(String baseUsername) {
        String username = baseUsername;
        int counter = 1;

        while (memberRepository.existsByUsername(username)) {
            String suffix = String.valueOf(counter);
            int maxBaseLength = 12 - suffix.length();
            
            if (baseUsername.length() > maxBaseLength) {
                username = baseUsername.substring(0, maxBaseLength) + suffix;
            } else {
                username = baseUsername + suffix;
            }
            
            counter++;
            
            // Safety check to prevent infinite loop
            if (counter > 9999) {
                username = "user" + System.currentTimeMillis() % 100000;
                break;
            }
        }

        return username;
    }

}

