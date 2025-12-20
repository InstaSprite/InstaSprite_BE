//package org.olaz.instasprite_be.domain.alarm.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import org.olaz.instasprite_be.domain.alarm.dto.AlarmType;
//import org.olaz.instasprite_be.domain.alarm.entity.Alarm;
//import org.olaz.instasprite_be.domain.alarm.repository.jdbc.AlarmRepositoryJdbc;
//import org.olaz.instasprite_be.domain.alarm.repository.querydsl.AlarmRepositoryQuerydsl;
//import org.olaz.instasprite_be.domain.feed.entity.Comment;
//import org.olaz.instasprite_be.domain.feed.entity.Post;
//import org.olaz.instasprite_be.domain.follow.entity.Follow;
//import org.olaz.instasprite_be.domain.member.entity.Member;
//
//public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmRepositoryQuerydsl, AlarmRepositoryJdbc {
//
//	void deleteByTypeAndAgentAndTargetAndPost(AlarmType type, Member agent, Member target, Post post);
//
//	void deleteByTypeAndAgentAndTargetAndComment(AlarmType type, Member agent, Member target, Comment comment);
//
//	void deleteByTypeAndAgentAndTargetAndFollow(AlarmType type, Member agent, Member target, Follow follow);
//
//	List<Alarm> findAllByPost(Post post);
//
//	List<Alarm> findAllByCommentIn(List<Comment> comments);
//
//}
