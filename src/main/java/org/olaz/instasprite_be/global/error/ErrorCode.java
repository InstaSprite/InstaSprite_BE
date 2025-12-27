package org.olaz.instasprite_be.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ErrorCode Convention
 * - Managed separately by domain
 * - Created in the form [SUBJECT_REASON]
 * - Codes use 1–2 letters from the domain name
 * - Messages end with "."
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Global
    INTERNAL_SERVER_ERROR(500, "G001", "Internal server error."),
    METHOD_NOT_ALLOWED(405, "G002", "HTTP method not allowed."),
    INPUT_VALUE_INVALID(400, "G003", "Invalid input value."),
    INPUT_TYPE_INVALID(400, "G004", "Invalid input type."),
    HTTP_MESSAGE_NOT_READABLE(400, "G005", "Request body is missing or contains invalid value type."),
    HTTP_HEADER_INVALID(400, "G006", "Invalid request header."),
    IMAGE_TYPE_NOT_SUPPORTED(400, "G007", "Unsupported image type."),
    FILE_CONVERT_FAIL(500, "G008", "File cannot be converted."),
    ENTITY_TYPE_INVALID(500, "G009", "Invalid entity type."),
    FILTER_MUST_RESPOND(500, "G010", "Request that should be processed by filter reached Controller."),
    RATE_LIMIT_EXCEEDED(429, "G011", "Too many requests. Please try again later."),

    // Member
    MEMBER_NOT_FOUND(400, "M001", "User does not exist."),
    USERNAME_ALREADY_EXISTS(400, "M002", "Username already exists."),
    AUTHENTICATION_FAIL(401, "M003", "Login required."),
    AUTHORITY_INVALID(403, "M004", "No permission."),
    BLOCK_ALREADY_EXISTS(400, "M005", "User already blocked."),
    UNBLOCK_FAILED(400, "M006", "Cannot unblock a user who is not blocked."),
    BLOCK_MYSELF_FAIL(400, "M007", "You cannot block yourself."),
    UNBLOCK_MYSELF_FAIL(400, "M008", "You cannot unblock yourself."),
    LOGOUT_BY_ANOTHER(401, "M009", "Logged out due to another device."),
    EMAIL_ALREADY_REGISTERED(400, "M010", "Email is already registered."),
    INVALID_CREDENTIALS(401, "M011", "Invalid username/email or password."),
    ACCOUNT_PROVIDER_MISMATCH(400, "M012", "Account is registered with a different provider."),
    TOTP_REQUIRED(401, "M013", "OTP code is required."),
    TOTP_INVALID(401, "M014", "Invalid OTP code."),
    TOTP_NOT_ENROLLED(400, "M015", "TOTP is not enrolled for this account."),
    TOTP_ALREADY_ENABLED(400, "M016", "TOTP is already enabled."),
    TOTP_NOT_ENABLED(400, "M017", "TOTP is not enabled."),
    EMAIL_NOT_VERIFIED(401, "M018", "Email is not verified."),
    EMAIL_VERIFY_TOKEN_INVALID(400, "M019", "Invalid email verification token."),
    EMAIL_VERIFY_TOKEN_EXPIRED(400, "M020", "Email verification token has expired."),
    EMAIL_ALREADY_VERIFIED(400, "M021", "Email is already verified."),

    // Follow
    FOLLOW_ALREADY_EXIST(400, "F001", "User already followed."),
    UNFOLLOW_FAIL(400, "F002", "Cannot unfollow a user you don’t follow."),
    FOLLOW_MYSELF_FAIL(400, "F003", "You cannot follow yourself."),
    UNFOLLOW_MYSELF_FAIL(400, "F004", "You cannot unfollow yourself."),
    FOLLOWER_DELETE_FAIL(400, "F005", "Cannot remove this follower."),

    // Jwt
    JWT_INVALID(401, "J001", "Invalid token."),
    JWT_EXPIRED(401, "J002", "Expired token."),
    EXPIRED_REFRESH_TOKEN(401, "J003", "Expired refresh token. Please log in again."),

    // OAuth
    OAUTH_PROVIDER_ERROR(500, "O001", "OAuth provider error occurred."),
    OAUTH_TOKEN_INVALID(401, "O002", "Invalid OAuth token."),
    OAUTH_TOKEN_EXPIRED(401, "O003", "OAuth token has expired."),
    GOOGLE_AUTH_FAIL(401, "O004", "Google authentication failed."),

    // Post
    POST_NOT_FOUND(400, "P001", "Post does not exist."),
    POST_CANT_DELETE(400, "P002", "Only the author can delete this post."),
    POST_LIKE_NOT_FOUND(400, "P003", "User has not liked this post."),
    POST_LIKE_ALREADY_EXIST(400, "P004", "User already liked this post."),
    POST_IMAGES_AND_ALT_TEXTS_MISMATCH(400, "P005", "Number of post images and alt texts must match."),
    POST_TAGS_EXCEED(400, "P006", "You can tag up to 20 users."),
    POST_IMAGE_REQUIRED(400, "P007", "At least one image is required for post."),

    // Bookmark
    BOOKMARK_ALREADY_EXIST(400, "BK001", "Post already bookmarked."),
    BOOKMARK_NOT_FOUND(400, "BK002", "Post has not been bookmarked yet."),
    BOOKMARK_MYSELF_FAIL(400, "BK003", "You cannot bookmark your own post."),

    // Comment
    COMMENT_NOT_FOUND(400, "CO001", "Comment does not exist."),
    COMMENT_CANT_DELETE(400, "CO002", "You cannot delete comments written by others."),
    COMMENT_LIKE_ALREADY_EXIST(400, "CO003", "User already liked this comment."),
    COMMENT_LIKE_NOT_FOUND(400, "CO004", "User has not liked this comment."),
    COMMENT_CANT_UPLOAD(400, "CO005", "Cannot write comments on a post with comments disabled."),
    REPLY_CANT_UPLOAD(400, "CO006", "Replies can only be added to top-level comments."),

    // Mention
    MENTION_MEMBER_NOT_FOUND(400, "ME001", "Mentioned user does not exist."),
    MENTION_DUPLICATE(400, "ME002", "User already mentioned in this comment."),

    // Alarm
    MISMATCHED_ALARM_TYPE(400, "A002", "Invalid notification type."),

    // Email
    EMAIL_SEND_FAIL(500, "E001", "Error occurred while sending email."),

    // HashTag
//    HASHTAG_NOT_FOUND(400, "H001", "Hashtag does not exist."),
    HASHTAG_FOLLOW_ALREADY_EXIST(400, "H002", "Hashtag already followed."),
    HASHTAG_FOLLOW_NOT_FOUND(400, "H003", "Hashtag has not been followed yet."),
    HASHTAG_PREFIX_MISMATCH(400, "H004", "Hashtag must start with #."),

    // Search
    SEARCH_RECORD_NOT_FOUND(400, "SE001", "Search record does not exist."),
    SEARCH_TYPE_INVALID(400, "SE002", "Invalid search type."),

    ;

    private final int status;
    private final String code;
    private final String message;

}
