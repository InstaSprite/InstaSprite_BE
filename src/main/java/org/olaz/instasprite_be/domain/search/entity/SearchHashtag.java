package org.olaz.instasprite_be.domain.search.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.olaz.instasprite_be.domain.hashtag.entity.Hashtag;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("HASHTAG")
@Table(name = "search_hashtags")
public class SearchHashtag extends Search {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hashtag_id")
	private Hashtag hashtag;

	public SearchHashtag(Hashtag hashtag) {
		super();
		this.hashtag = hashtag;
	}

}

