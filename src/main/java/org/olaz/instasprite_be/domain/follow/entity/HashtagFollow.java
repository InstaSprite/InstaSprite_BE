//package org.olaz.instasprite_be.domain.follow.entity;
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
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import org.olaz.instasprite_be.domain.hashtag.entity.Hashtag;
//import org.olaz.instasprite_be.domain.member.entity.Member;
//
//@Getter
//@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Table(name = "hashtag_follows")
//public class HashtagFollow {
//
//	@Id
//	@Column(name = "hashtag_follow_id")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "member_id")
//	private Member member;
//
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "hashtag_id")
//	private Hashtag hashtag;
//
//	@Builder
//	public HashtagFollow(Member member, Hashtag hashtag) {
//		this.member = member;
//		this.hashtag = hashtag;
//	}
//
//}
