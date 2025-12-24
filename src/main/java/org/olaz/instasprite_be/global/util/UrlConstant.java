package org.olaz.instasprite_be.global.util;

/**
 * RESTful URL Constants for InstaSprite API
 * Social media platform for pixel art sharing
 *
 * RESTful Principles:
 * - Use nouns for resources, not verbs
 * - Use HTTP methods (GET, POST, PUT, DELETE) for actions
 * - Use plural nouns for collections
 * - Use path parameters for resource IDs
 * - Use query parameters for filtering/sorting
 */
public class UrlConstant {

    // ============================================
    // BASE API URL
    // ============================================
    public static final String API_BASE_V1 = "/api/v1";

    // ============================================
    // AUTH URLs
    // ============================================
    public static final String AUTH = "/auth";
    public static final String AUTH_GOOGLE = AUTH + "/google";        // POST - login with Google
    public static final String AUTH_REFRESH = AUTH + "/refresh";      // POST - refresh token     // POST - logout
    public static final String AUTH_LOGIN = AUTH + "/login";          // POST - local login
    public static final String AUTH_REGISTER = AUTH + "/register";    // POST - local register

    // ============================================
    // MEMBERS URLs (Users) - "/accounts" mapping in controllers
    // ============================================
    public static final String ACCOUNTS = "/accounts";                              // Base path for member operations
    public static final String ACCOUNTS_PROFILE = ACCOUNTS + "/profile";           // GET - current user's menu profile
    public static final String ACCOUNTS_USERNAME = ACCOUNTS + "/{username}";       // GET - user profile by username
    public static final String ACCOUNTS_USERNAME_WITHOUT = ACCOUNTS_USERNAME + "/without";  // GET - user profile without login
    public static final String ACCOUNTS_USERNAME_MINI = ACCOUNTS_USERNAME + "/mini";        // GET - mini profile
    public static final String ACCOUNTS_IMAGE = ACCOUNTS + "/image";                        // POST - upload, DELETE - delete profile image
    public static final String ACCOUNTS_EDIT = ACCOUNTS + "/edit";                          // GET, PUT - profile edit
    
    // Member's posts endpoints
    public static final String ACCOUNTS_USERNAME_POSTS_RECENT = ACCOUNTS_USERNAME + "/posts/recent";      // GET - 15 recent posts
    public static final String ACCOUNTS_USERNAME_POSTS_RECENT_POST = ACCOUNTS_USERNAME + "/posts/recent/post";  // GET - 6 recent posts
    public static final String ACCOUNTS_USERNAME_POSTS = ACCOUNTS_USERNAME + "/posts";                    // GET - posts with pagination
    public static final String ACCOUNTS_USERNAME_POSTS_RECENT_WITHOUT = ACCOUNTS_USERNAME + "/posts/recent/without";  // GET - 15 recent posts without login
    public static final String ACCOUNTS_USERNAME_POSTS_WITHOUT = ACCOUNTS_USERNAME + "/posts/without";    // GET - posts without login
    public static final String ACCOUNTS_POSTS_SAVED_RECENT = ACCOUNTS + "/posts/saved/recent";           // GET - 15 recent saved posts
    public static final String ACCOUNTS_POSTS_SAVED = ACCOUNTS + "/posts/saved";                          // GET - saved posts with pagination


    // ============================================
    // POSTS URLs
    // ============================================
    public static final String POSTS = "/posts";
    public static final String POST_RECENT_PATH = "/recent";                        // GET - 10 most recent posts
    public static final String POST_ID_PATH = "/{postId}";                          // GET - get post, DELETE - delete post
    public static final String POST_ID_WITHOUT_PATH = "/{postId}/without";          // GET - get post without login
    public static final String POST_LIKE_PATH = "/like";                            // POST - like, DELETE
    public static final String POST_ID_LIKES_PATH = "/{postId}/likes";              // GET - list users who liked
    public static final String POST_SAVE_PATH = "/save";                            // POST - bookmark, DELETE

    // ============================================
    // COMMENTS URLs
    // ============================================
    public static final String COMMENTS = "/comments";
    public static final String COMMENT_ID_PATH = "/{commentId}";                    // GET - get replies, DELETE - delete comment
    public static final String COMMENT_POSTS_ID_PATH = "/posts/{postId}";           // GET - comments for a post
    public static final String COMMENT_LIKE_PATH = "/like";                         // POST - like, DELETE - unlike (uses @RequestParam commentId)
    public static final String COMMENT_ID_LIKES_PATH = "/{commentId}/likes";        // GET - list users who liked comment


    // ============================================
    // POSTS_ID_COMMENTS - Link between Posts and Comments
    // ============================================
//    public static final String POSTS_ID_COMMENTS = "/posts/{postId}/comments";  // GET - list comments, POST - create comment



    // ============================================
    // FOLLOWS URLs (Relationship Resource)
    // ============================================
    public static final String FOLLOW_MEMBER_USERNAME = "/{followMemberUsername}/follow";      // POST - follow, DELETE - unfollow
    public static final String FOLLOWER_MEMBER_USERNAME = "/{followMemberUsername}/follower";  // DELETE - remove follower
    public static final String FOLLOWING_MEMBER_USERNAME = "/{memberUsername}/following";      // GET - get following list
    public static final String FOLLOWERS_MEMBER_USERNAME = "/{memberUsername}/followers";      // GET - get followers list


    // ============================================
    // HASHTAGS URLs
    // ============================================
//    public static final String HASHTAGS = "/hashtags";
//    public static final String HASHTAGS_FOLLOW_PATH = "/follow";                               // POST - follow, DELETE - unfollow (relative path)
//    public static final String HASHTAGS_NAME_PATH = "/{hashtag}";                              // GET - hashtag profile (relative path)
//    public static final String HASHTAGS_NAME_WITHOUT_PATH = "/{hashtag}/without";              // GET - hashtag profile without login (relative path)
//
//    // Full paths (for reference)
//    public static final String HASHTAGS_FOLLOW = HASHTAGS + HASHTAGS_FOLLOW_PATH;              // POST - follow, DELETE - unfollow hashtag (uses @RequestParam)
//    public static final String HASHTAGS_NAME = HASHTAGS + HASHTAGS_NAME_PATH;                  // GET - hashtag profile
//    public static final String HASHTAGS_NAME_WITHOUT = HASHTAGS + HASHTAGS_NAME_WITHOUT_PATH;  // GET - hashtag profile without login


    // ============================================
    // SEARCH URLs
    // ============================================
    public static final String SEARCH = "/search";                                // GET - unified search (member or post)
//    public static final String TOP_SEARCH = "/topsearch";                                       // GET - general search
//    public static final String TOP_SEARCH_RECOMMEND = TOP_SEARCH + "/recommend";                 // GET - recommended members
//    public static final String TOP_SEARCH_AUTO_MEMBER = TOP_SEARCH + "/auto/member";             // GET - member autocomplete
//    public static final String TOP_SEARCH_AUTO_HASHTAG = TOP_SEARCH + "/auto/hashtag";           // GET - hashtag autocomplete
//    public static final String TOP_SEARCH_MARK = TOP_SEARCH + "/mark";                           // POST - mark search & update history
//    public static final String TOP_SEARCH_RECENT_TOP = TOP_SEARCH + "/recent/top";               // GET - top 15 recent searches
//    public static final String TOP_SEARCH_RECENT = TOP_SEARCH + "/recent";                       // GET - recent search history, DELETE - delete recent search
//    public static final String TOP_SEARCH_RECENT_ALL = TOP_SEARCH + "/recent/all";               // DELETE - clear all recent searches




    // ============================================
    // NOTIFICATIONS URLs (Alarms/Notifications)
    // ============================================
//    public static final String ALARMS = "/alarms";                                             // GET - list alarms/notifications


    // ============================================
    // BLOCKS URLs (Blocked Users)
    // ============================================
//    public static final String BLOCKS = "/blocks";                          // GET - list blocked users (not implemented)
//    public static final String BLOCKS_USERNAME = BLOCKS + "/{username}";    // POST - block, DELETE - unblock
}
