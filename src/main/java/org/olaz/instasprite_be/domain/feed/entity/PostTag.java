package org.olaz.instasprite_be.domain.feed.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.querydsl.core.annotations.QueryInit;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.olaz.instasprite_be.domain.feed.vo.Tag;

@Getter
@Entity
@Table(name = "post_tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTag {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_tag_id")
	private Long id;

	/**
	 * @QueryInit : to resolve Querydsl's depth limit
	 * @Ref https://github.com/querydsl/querydsl/issues/2129
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_image_id")
	@QueryInit({"*.*", "post.member"})
	private PostImage postImage;

	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name = "username", column = @Column(name = "post_tag_username"))
	})
	private Tag tag;

	@Builder
	public PostTag(PostImage postImage, Tag tag) {
		this.postImage = postImage;
		this.tag = tag;
	}

}
