//package org.olaz.instasprite_be.domain.search.dto;
//
//import com.querydsl.core.annotations.QueryProjection;
//
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import org.olaz.instasprite_be.domain.hashtag.entity.Hashtag;
//
//@Getter
//@Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class SearchHashtagDto extends SearchDto {
//
//	private String name;
//	private Integer postCount;
//
//	@QueryProjection
//	public SearchHashtagDto(String dtype, Hashtag hashtag) {
//		super(dtype);
//		this.name = hashtag.getName();
//		this.postCount = hashtag.getCount();
//	}
//
//}
