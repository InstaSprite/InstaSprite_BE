package org.olaz.instasprite_be.domain.feed.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import org.olaz.instasprite_be.domain.feed.entity.Post;
import org.olaz.instasprite_be.domain.feed.entity.PostLike;
import org.olaz.instasprite_be.domain.feed.repository.PostLikeRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

	private final PostLikeRepository postLikeRepository;

	@Transactional
	public void deleteAll(Post post) {
		final List<PostLike> postLikes = postLikeRepository.findAllByPost(post);
		postLikeRepository.deleteAllInBatch(postLikes);
	}

	public List<PostLike> getAllWithMember(Long postId) {
		return postLikeRepository.findAllWithMemberByPostId(postId);
	}

}
