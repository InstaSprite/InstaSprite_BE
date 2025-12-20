//package org.olaz.instasprite_be.domain.hashtag.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import org.olaz.instasprite_be.domain.feed.entity.Post;
//
//@Getter
//@Entity
//@Table(name = "hashtag_posts")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class HashtagPost {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "hashtag_post_id")
//	private Long id;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "hashtag_id")
//	private Hashtag hashtag;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "post_id")
//	private Post post;
//
//	public HashtagPost(Hashtag hashtag, Post post) {
//		this.hashtag = hashtag;
//		this.post = post;
//	}
//
//}
