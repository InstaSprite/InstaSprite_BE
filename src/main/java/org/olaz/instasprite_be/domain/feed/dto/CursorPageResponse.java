package org.olaz.instasprite_be.domain.feed.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CursorPageResponse<T> {
	private List<T> content;
	private Long nextCursor;
	private boolean hasNext;

	public CursorPageResponse(List<T> content, Long nextCursor) {
		this.content = content;
		this.nextCursor = nextCursor;
		this.hasNext = nextCursor != null;
	}

	public static <T> CursorPageResponse<T> of(List<T> content, Long nextCursor) {
		return new CursorPageResponse<>(content, nextCursor);
	}
}


