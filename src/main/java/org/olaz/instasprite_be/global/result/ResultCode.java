package org.olaz.instasprite_be.global.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ResultCode Convention
 * - Managed separately by domain
 * - Created in the form [VERB_OBJECT_SUCCESS]
 * - Codes use 1–2 letters from the domain name
 * - Messages end with "."
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

	// Member - Authentication
	GOOGLE_LOGIN_SUCCESS(200, "M001", "Google login was successful."),
	GOOGLE_REGISTER_SUCCESS(200, "M002", "Google registration was successful."),
	REISSUE_SUCCESS(200, "M003", "Token reissue was successful."),
	TOKEN_REFRESH_SUCCESS(200, "M004", "Token refresh was successful."),
	LOGOUT_SUCCESS(200, "M005", "Logged out successfully."),
	LOCAL_REGISTER_SUCCESS(200, "M006", "Local registration was successful."),
	LOCAL_LOGIN_SUCCESS(200, "M007", "Local login was successful."),
	TOTP_ENROLL_SUCCESS(200, "M030", "TOTP enrollment was generated."),
	TOTP_ENABLE_SUCCESS(200, "M031", "TOTP was enabled."),
	TOTP_DISABLE_SUCCESS(200, "M032", "TOTP was disabled."),
	TOTP_STATUS_SUCCESS(200, "M033", "TOTP status was retrieved."),
	EMAIL_VERIFY_SUCCESS(200, "M034", "Email was verified."),
	EMAIL_VERIFY_RESEND_SUCCESS(200, "M035", "Verification email was sent."),
	FCM_TOKEN_UPSERT_SUCCESS(200, "M036", "FCM token was saved."),
	FCM_TOKEN_DELETE_SUCCESS(200, "M037", "FCM token was deleted."),
	PASSWORD_RESET_EMAIL_SENT(200, "M038", "Password reset email was sent."),
	PASSWORD_RESET_SUCCESS(200, "M039", "Password was reset successfully."),

	// Member - Profile
	GET_USER_PROFILE_SUCCESS(200, "M010", "Retrieved user profile."),
	GET_MINIPROFILE_SUCCESS(200, "M011", "Retrieved mini profile."),
	GET_MENU_MEMBER_SUCCESS(200, "M012", "Retrieved top menu profile."),
	GET_EDIT_PROFILE_SUCCESS(200, "M013", "Retrieved profile edit information."),
	EDIT_PROFILE_SUCCESS(200, "M014", "Profile updated successfully."),
	
	// Member - Username & Images
	CHECK_USERNAME_GOOD(200, "M020", "Username is available."),
	CHECK_USERNAME_BAD(200, "M021", "Username is not available."),
	UPLOAD_MEMBER_IMAGE_SUCCESS(200, "M022", "Member image uploaded."),
	DELETE_MEMBER_IMAGE_SUCCESS(200, "M023", "Member image deleted."),

	// Alarm
	GET_ALARMS_SUCCESS(200, "A001", "Successfully retrieved alarms."),

	// Follow
	FOLLOW_SUCCESS(200, "F001", "Followed member successfully."),
	UNFOLLOW_SUCCESS(200, "F002", "Unfollowed member successfully."),
	GET_FOLLOWINGS_SUCCESS(200, "F003", "Retrieved following list."),
	GET_FOLLOWERS_SUCCESS(200, "F004", "Retrieved follower list."),
	DELETE_FOLLOWER_SUCCESS(200, "F005", "Follower removed successfully."),
	FOLLOW_FAIL(200, "F006", "Cannot follow this target."),

	// Block
	BLOCK_SUCCESS(200, "B001", "Member blocked successfully."),
	UNBLOCK_SUCCESS(200, "B002", "Member unblocked successfully."),

	// Post
	CREATE_POST_SUCCESS(200, "P001", "Post uploaded successfully."),
	DELETE_POST_SUCCESS(200, "P002", "Post deleted successfully."),
	UPDATE_POST_SUCCESS(200, "P003", "Post updated successfully."),
	GET_POST_SUCCESS(200, "P004", "Retrieved post."),
	GET_POST_PAGE_SUCCESS(200, "P005", "Retrieved post list page."),
	GET_RECENT_POSTS_SUCCESS(200, "P006", "Retrieved recent posts."),
	LIKE_POST_SUCCESS(200, "P007", "Liked post successfully."),
	UNLIKE_POST_SUCCESS(200, "P008", "Unliked post successfully."),
	GET_POST_LIKES_SUCCESS(200, "P009", "Retrieved list of users who liked the post."),
	GET_MEMBER_POSTS_SUCCESS(200, "P010", "Retrieved member's posts."),
	GET_MEMBER_TAGGED_POSTS_SUCCESS(200, "P011", "Retrieved member's tagged posts."),
	MOST_LIKED_POSTS_SUCCESS(200, "P012", "Retrieved more liked posts."),

	// Bookmark
	BOOKMARK_POST_SUCCESS(200, "BK001", "Bookmarked post successfully."),
	UNBOOKMARK_POST_SUCCESS(200, "BK002", "Removed post bookmark successfully."),
	GET_BOOKMARKED_POSTS_SUCCESS(200, "BK003", "Retrieved bookmarked posts."),

	// Comment
	CREATE_COMMENT_SUCCESS(200, "CO001", "Comment uploaded successfully."),
	DELETE_COMMENT_SUCCESS(200, "CO002", "Comment deleted successfully."),
	UPDATE_COMMENT_SUCCESS(200, "CO003", "Comment updated successfully."),
	GET_COMMENT_PAGE_SUCCESS(200, "CO004", "Retrieved comment list page."),
	GET_REPLY_PAGE_SUCCESS(200, "CO005", "Retrieved reply list page."),
	LIKE_COMMENT_SUCCESS(200, "CO006", "Liked comment successfully."),
	UNLIKE_COMMENT_SUCCESS(200, "CO007", "Unliked comment successfully."),
	GET_COMMENT_LIKES_SUCCESS(200, "CO008", "Retrieved list of users who liked the comment."),

	// Hashtag
	GET_HASHTAGS_SUCCESS(200, "H001", "Retrieved hashtag list with pagination."),
	FOLLOW_HASHTAG_SUCCESS(200, "H002", "Followed hashtag successfully."),
	UNFOLLOW_HASHTAG_SUCCESS(200, "H003", "Unfollowed hashtag successfully."),
	GET_HASHTAG_PROFILE_SUCCESS(200, "H004", "Retrieved hashtag profile."),
	GET_HASHTAG_POSTS_SUCCESS(200, "H005", "Retrieved hashtag post list with pagination."),

	// Search
	SEARCH_SUCCESS(200, "SE001", "Search was successful."),
	MARK_SEARCHED_ENTITY_SUCCESS(200, "SE002", "Increased search view count and updated recent search history."),
	GET_RECENT_SEARCH_SUCCESS(200, "SE003", "Retrieved recent search records."),
	DELETE_RECENT_SEARCH_SUCCESS(200, "SE004", "Deleted recent search record."),
	DELETE_ALL_RECENT_SEARCH_SUCCESS(200, "SE005", "Deleted all recent search records."),
	GET_MEMBER_AUTO_COMPLETE_SUCCESS(200, "SE006", "Retrieved member auto-complete results."),
	GET_HASHTAG_AUTO_COMPLETE_SUCCESS(200, "SE007", "Retrieved hashtag auto-complete results."),
	GET_RECOMMENDED_MEMBERS_SUCCESS(200, "SE008", "Retrieved recommended members to follow.");

	private final int status;
	private final String code;
	private final String message;

}
