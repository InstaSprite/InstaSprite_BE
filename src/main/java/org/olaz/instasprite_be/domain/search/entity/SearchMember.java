//package org.olaz.instasprite_be.domain.search.entity;
//
//import jakarta.persistence.DiscriminatorValue;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.OneToOne;
//import jakarta.persistence.Table;
//
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import org.olaz.instasprite_be.domain.member.entity.Member;
//
//@Getter
//@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@DiscriminatorValue("MEMBER")
//@Table(name = "search_members")
//public class SearchMember extends Search {
//
//	@OneToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "member_id")
//	private Member member;
//
//	public SearchMember(Member member) {
//		super();
//		this.member = member;
//	}
//
//}
