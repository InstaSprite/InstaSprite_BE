package org.olaz.instasprite_be.domain.member.entity;

import java.util.List;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import org.olaz.instasprite_be.domain.follow.entity.Follow;
import org.olaz.instasprite_be.global.vo.Image;
import org.olaz.instasprite_be.global.vo.ImageType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member")
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_username", nullable = false, length = 20, unique = true)
    private String username;

    @Column(name = "member_google_id", unique = true)
    private String googleId;

    @Column(name = "member_password", length = 255)
    private String password;

    @Column(name = "member_provider", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private MemberProvider provider;

    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Column(name = "member_name", nullable = false, length = 20)
    private String name;

    @Column(name = "member_bio", columnDefinition = "TEXT")
    private String introduce;

    @Column(name = "member_email", unique = true)
    private String email;

    @Column(name = "member_email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "member_totp_enabled", nullable = false)
    private boolean totpEnabled = false;

    /**
     * Encrypted (or plaintext) shared secret for RFC-6238 TOTP.
     * Stored as Base64(iv+ciphertext) when encryption is enabled.
     */
    @Column(name = "member_totp_secret", columnDefinition = "TEXT")
    private String totpSecret;

    @OneToMany(mappedBy = "member")
    private List<Follow> followings;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "imageUrl", column = @Column(name = "member_image_url")),
            @AttributeOverride(name = "imageType", column = @Column(name = "member_image_type")),
            @AttributeOverride(name = "imageName", column = @Column(name = "member_image_name")),
            @AttributeOverride(name = "imageUUID", column = @Column(name = "member_image_uuid"))
    })
    private Image image;

    @Builder
    public Member(String username, String name, String googleId, String email, String password, MemberProvider provider) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.googleId = googleId;
        this.password = password;
        this.provider = provider != null ? provider : MemberProvider.GOOGLE;

        this.role = MemberRole.ROLE_USER;
        this.emailVerified = this.provider == MemberProvider.GOOGLE;
        this.image = Image.builder()
                .imageName("default")
                .imageType(ImageType.JPEG)
                .imageUrl("default.jpeg")
                .imageUUID("base-UUID")
                .build();
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public void uploadImage(Image image) {
        deleteImage();
        this.image = image;
    }

    public void deleteImage() {
        if (this.image.getImageUUID().equals("base-UUID"))
            return;

        this.image = Image.builder()
                .imageName("default")
                .imageType(ImageType.PNG)
                .imageUrl("default.png")
                .imageUUID("base-UUID")
                .build();
    }

    public void setupTotpSecret(String totpSecret) {
        this.totpSecret = totpSecret;
        this.totpEnabled = false;
    }

    public void enableTotp() {
        this.totpEnabled = true;
    }

    public void disableTotp() {
        this.totpEnabled = false;
        this.totpSecret = null;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

}
