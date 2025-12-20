package org.olaz.instasprite_be.domain.member.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.querydsl.core.annotations.QueryProjection;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import org.olaz.instasprite_be.domain.follow.dto.FollowDto;
import org.olaz.instasprite_be.global.vo.Image;

@Schema(description = "User profile response model")
@Getter
public class UserProfileResponse {

	@Schema(description = "Username", example = "dlwlrma")
	private String memberUsername;

	@Schema(description = "Name", example = "John Doe")
	private String memberName;

	@Schema(description = "Profile image")
	private Image memberImage;

	@Schema(description = "Following status", example = "true")
	private boolean isFollowing;

	@Schema(description = "Follower status", example = "false")
	private boolean isFollower;

	@Schema(description = "Introduction", example = "Hello everyone")
	private String memberIntroduce;

	@Schema(description = "Post count", example = "90")
	private Long memberPostsCount;

	@Schema(description = "Following count", example = "100")
	private Long memberFollowingsCount;

	@Schema(description = "Follower count", example = "100")
	private Long memberFollowersCount;

	@Schema(description = "Following members who follow this user", example = "dlwlrma")
	private List<FollowDto> followingMemberFollow;
	private int followingMemberFollowCount;

	@Schema(description = "Is current user", example = "false")
	private boolean isMe;


	@QueryProjection
	public UserProfileResponse(String username, String name, Image image,
		boolean isFollowing, boolean isFollower,
		String introduce, Long postsCount, Long followingsCount, Long followersCount,
		boolean isMe) {
		this.memberUsername = username;
		this.memberName = name;
		this.memberImage = image;
		this.isFollowing = isFollowing;
		this.isFollower = isFollower;
		this.memberIntroduce = introduce;
		this.memberPostsCount = postsCount;
		this.memberFollowingsCount = followingsCount;
		this.memberFollowersCount = followersCount;
		this.isMe = isMe;
	}

	public void setFollowingMemberFollow(List<FollowDto> followingMemberFollow, int maxCount) {
		this.followingMemberFollow = followingMemberFollow
			.stream()
			.limit(maxCount)
			.collect(Collectors.toList());
		this.followingMemberFollowCount = followingMemberFollow.size() - this.followingMemberFollow.size();
	}
}

