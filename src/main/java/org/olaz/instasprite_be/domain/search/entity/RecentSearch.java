//package org.olaz.instasprite_be.domain.search.entity;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.Id;
//import jakarta.persistence.IdClass;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.Table;
//
//import lombok.AccessLevel;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import org.olaz.instasprite_be.domain.member.entity.Member;
//
//@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Entity
//@Table(name = "recent_searches")
//@IdClass(RecentSearch.RecentSearchId.class)
//public class RecentSearch {
//
//	@Id
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "member_id")
//	private Member member;
//	@Id
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "search_id")
//	private Search search;
//	@Column(name = "recent_search_last_searched_date")
//	private LocalDateTime lastSearchedDate;
//
//	@Builder
//	public RecentSearch(Member member, Search search) {
//		this.member = member;
//		this.search = search;
//	}
//
//	public void updateLastSearchedDate() {
//		this.lastSearchedDate = LocalDateTime.now();
//	}
//
//	@NoArgsConstructor(access = AccessLevel.PROTECTED)
//	@AllArgsConstructor(access = AccessLevel.PROTECTED)
//	static class RecentSearchId implements Serializable {
//
//		private Long member;
//		private Long search;
//
//		@Override
//		public boolean equals(Object o) {
//			if (this == o) return true;
//			if (o == null || getClass() != o.getClass()) return false;
//			RecentSearchId that = (RecentSearchId) o;
//			return java.util.Objects.equals(member, that.member) &&
//				   java.util.Objects.equals(search, that.search);
//		}
//
//		@Override
//		public int hashCode() {
//			return java.util.Objects.hash(member, search);
//		}
//	}
//
//}
