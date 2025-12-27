package org.olaz.instasprite_be.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.olaz.instasprite_be.domain.member.entity.Member;
import org.olaz.instasprite_be.domain.member.entity.MemberProvider;
import org.olaz.instasprite_be.domain.member.exception.EmailAlreadyVerifiedException;
import org.olaz.instasprite_be.domain.member.repository.MemberRepository;
import org.olaz.instasprite_be.domain.member.service.EmailVerificationService;
import org.olaz.instasprite_be.domain.member.exception.EmailVerifyTokenExpiredException;
import org.olaz.instasprite_be.domain.member.exception.EmailVerifyTokenInvalidException;
import org.olaz.instasprite_be.domain.member.exception.ProviderMismatchException;
import org.olaz.instasprite_be.global.result.ResultResponse;
import org.olaz.instasprite_be.global.util.AuthUtil;
import org.olaz.instasprite_be.global.util.UrlConstant;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.olaz.instasprite_be.global.result.ResultCode.*;

@Slf4j
@Tag(name = "Email Verification API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(UrlConstant.API_BASE_V1)
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;
    private final MemberRepository memberRepository;
    private final AuthUtil authUtil;

    @Operation(summary = "Verify email via token (from link)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email verified."),
            @ApiResponse(responseCode = "400", description = "Invalid/expired token.")
    })
    @GetMapping(UrlConstant.AUTH_EMAIL_VERIFY)
    public ResponseEntity<String> verify(@RequestParam(value = "token", required = false) String token) {
        try {
            emailVerificationService.verifyByToken(token);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(renderHtml("Email verified", "Your email is verified. You can close this tab.", true));
        } catch (EmailVerifyTokenExpiredException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_HTML)
                    .body(renderHtml("Link expired", "This verification link has expired. Please request a new one.", false));
        } catch (EmailVerifyTokenInvalidException e) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_HTML)
                    .body(renderHtml("Invalid link", "This verification link is invalid. Please request a new one.", false));
        }
    }

    @Operation(summary = "Resend verification email (LOCAL only)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Verification email sent."),
            @ApiResponse(responseCode = "400", description = "Email already verified / provider mismatch."),
            @ApiResponse(responseCode = "401", description = "Login required.")
    })
    @PostMapping(UrlConstant.AUTH_EMAIL_RESEND)
    public ResponseEntity<ResultResponse> resend() {
        Member me = authUtil.getLoginMember();
        if (me.getProvider() != MemberProvider.LOCAL) {
            throw new ProviderMismatchException();
        }
        if (me.isEmailVerified()) {
            throw new EmailAlreadyVerifiedException();
        }
        emailVerificationService.sendVerificationEmailForLocalMember(me);

        return ResponseEntity.ok(ResultResponse.of(EMAIL_VERIFY_RESEND_SUCCESS));
    }

    private static String renderHtml(String title, String message, boolean success) {
        String accent = success ? "#16a34a" : "#dc2626";
        String badge = success ? "SUCCESS" : "FAILED";
        return """
                <!doctype html>
                <html lang="en">
                <head>
                  <meta charset="utf-8" />
                  <meta name="viewport" content="width=device-width, initial-scale=1" />
                  <title>%s</title>
                  <style>
                    :root { color-scheme: light dark; }
                    body { margin: 0; font-family: ui-sans-serif, system-ui, -apple-system, Segoe UI, Roboto, Arial, "Apple Color Emoji","Segoe UI Emoji"; }
                    .wrap { min-height: 100vh; display: grid; place-items: center; padding: 24px; }
                    .card { width: 100%%; max-width: 520px; border: 1px solid rgba(127,127,127,.25); border-radius: 16px; padding: 20px; }
                    .badge { display: inline-block; padding: 6px 10px; border-radius: 999px; font-weight: 700; font-size: 12px; letter-spacing: .08em; color: %s; border: 1px solid %s; }
                    h1 { margin: 12px 0 8px; font-size: 22px; }
                    p { margin: 0; opacity: .85; line-height: 1.5; }
                  </style>
                </head>
                <body>
                  <div class="wrap">
                    <div class="card">
                      <span class="badge">%s</span>
                      <h1>%s</h1>
                      <p>%s</p>
                    </div>
                  </div>
                </body>
                </html>
                """.formatted(escape(title), accent, accent, badge, escape(title), escape(message));
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}


