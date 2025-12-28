package org.olaz.instasprite_be.domain.notification.service;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
import org.olaz.instasprite_be.domain.notification.repository.MemberFcmTokenRepository;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmNotificationService {

    private final ObjectProvider<FirebaseMessaging> firebaseMessagingProvider;
    private final MemberFcmTokenRepository memberFcmTokenRepository;
    private final MemberRepository memberRepository;

    public void sendEmailVerified(Long memberId) {
        FirebaseMessaging messaging = firebaseMessagingProvider.getIfAvailable();
        if (messaging == null) {
            log.warn("FCM sendEmailVerified skipped: FirebaseMessaging not available (fcm.enabled=false or bean not configured) memberId={}", memberId);
            return;
        }

        List<String> tokens = memberFcmTokenRepository.findTokensByMemberId(memberId);
        if (tokens.isEmpty()) {
            log.debug("FCM sendEmailVerified skipped: no tokens found for memberId={}", memberId);
            return;
        }

        String username = memberRepository.findById(memberId)
                .map(member -> member.getUsername())
                .orElse("unknown");

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(Notification.builder()
                        .setTitle("Email verified")
                        .setBody("Your email has been verified.")
                        .build())
                .putData("type", "EMAIL_VERIFIED")
                .putData("memberId", String.valueOf(memberId))
                .putData("username", username)
                .setAndroidConfig(AndroidConfig.builder()
                        .setPriority(AndroidConfig.Priority.HIGH)
                        .build())

                .build();

        try {
            log.info("FCM sendEmailVerified sending to Firebase: memberId={}, tokenCount={}", memberId, tokens.size());
            BatchResponse response = messaging.sendEachForMulticast(message);
            List<SendResponse> responses = response.getResponses();
            
            int successCount = 0;
            int failureCount = 0;
            for (int i = 0; i < responses.size(); i++) {
                SendResponse r = responses.get(i);
                if (r.isSuccessful()) {
                    successCount++;
                    log.debug("FCM sendEmailVerified success: memberId={}, tokenIndex={}, messageId={}", 
                            memberId, i, r.getMessageId());
                } else {
                    failureCount++;
                    log.warn("FCM sendEmailVerified failure: memberId={}, tokenIndex={}, error={}", 
                            memberId, i, r.getException());
                    cleanupIfUnregistered(memberId, tokens.get(i), r.getException());
                }
            }
            log.info("FCM sendEmailVerified completed: memberId={}, success={}, failure={}, total={}", 
                    memberId, successCount, failureCount, responses.size());
        } catch (Exception e) {
            log.error("FCM sendEmailVerified failed memberId={}", memberId, e);
        }
    }

    private void cleanupIfUnregistered(Long memberId, String token, FirebaseMessagingException ex) {
        if (ex == null) {
            return;
        }
        MessagingErrorCode code = ex.getMessagingErrorCode();
        if (code == MessagingErrorCode.UNREGISTERED) {
            memberFcmTokenRepository.deleteByMember_IdAndToken(memberId, token);
        }
    }
}


