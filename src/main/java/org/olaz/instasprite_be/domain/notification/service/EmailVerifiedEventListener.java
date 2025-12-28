package org.olaz.instasprite_be.domain.notification.service;

import lombok.RequiredArgsConstructor;
import org.olaz.instasprite_be.domain.notification.event.EmailVerifiedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
public class EmailVerifiedEventListener {

    private final FcmNotificationService fcmNotificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onEmailVerified(EmailVerifiedEvent event) {
        fcmNotificationService.sendEmailVerified(event.memberId());
    }
}


