package com.toy.everyday.config;

import com.github.javafaker.Faker;
import com.toy.everyday.entity.Comment;
import com.toy.everyday.entity.Member;
import com.toy.everyday.entity.MemberComment;
import com.toy.everyday.entity.MemberPost;
import com.toy.everyday.entity.Post;
import com.toy.everyday.repository.CommentRepository;
import com.toy.everyday.repository.MemberCommentRepository;
import com.toy.everyday.repository.MemberPostRepository;
import com.toy.everyday.repository.MemberRepository;
import com.toy.everyday.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DataInitializerService {

	private final MemberRepository memberRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final MemberPostRepository memberPostRepository;       // 주입
	private final MemberCommentRepository memberCommentRepository; // 주입
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void generateData(int memberCount, int postCount, int commentCount, int memberPostCount, int memberCommentCount) {
		// 한글 데이터를 위한 Faker 설정
		Faker faker = new Faker(new Locale("ko"));

		// 1. Member 생성
		List<Member> members = new ArrayList<>();
		for (int i = 0; i < memberCount; i++) {
			Member member = Member.builder()
				.username(faker.name().fullName())
				.email(faker.internet().emailAddress())
				.password(passwordEncoder.encode("1234")) // 비밀번호는 일괄 지정
				.build();
			members.add(member);
		}
		memberRepository.saveAll(members); // 한번에 저장하여 성능 향상

		// 2. Post 생성
		List<Post> posts = new ArrayList<>();
		for (int i = 0; i < postCount; i++) {
			Post post = Post.builder()
				.subject(faker.lorem().sentence()) // 제목
				.content(String.join("\n", faker.lorem().paragraphs(3))) // 내용
				.createDate(LocalDateTime.now())
				.author(members.get(faker.random().nextInt(members.size()))) // 랜덤 작성자
				.build();
			posts.add(post);
		}
		postRepository.saveAll(posts);

		// 3. Comment 생성
		List<Comment> comments = new ArrayList<>();
		for (int i = 0; i < commentCount; i++) {
			Comment comment = Comment.builder()
				.content(faker.lorem().sentence())
				.createDate(LocalDateTime.now())
				.author(members.get(faker.random().nextInt(members.size()))) // 랜덤 작성자
				.post(posts.get(faker.random().nextInt(posts.size()))) // 랜덤 게시글
				.build();
			comments.add(comment);
		}
		commentRepository.saveAll(comments);

		List<MemberPost> memberPosts = new ArrayList<>();
		Set<String> existingPostLikes = new HashSet<>(); // 중복 방지용 Set
		for (int i = 0; i < memberPostCount; i++) {
			Member randomMember = members.get(faker.random().nextInt(members.size()));
			Post randomPost = posts.get(faker.random().nextInt(posts.size()));

			String likeKey = randomMember.getId() + ":" + randomPost.getId();
			if (existingPostLikes.contains(likeKey)) { // 이미 좋아요를 누른 경우
				continue; // 건너뛰기
			}
			existingPostLikes.add(likeKey);

			MemberPost memberPost = MemberPost.builder()
				.member(randomMember)
				.post(randomPost)
				.build();
			memberPosts.add(memberPost);
		}
		memberPostRepository.saveAll(memberPosts);

		// =========================================================
		// 5. MemberComment (댓글 좋아요) 생성 - 추가된 부분
		// =========================================================
		List<MemberComment> memberComments = new ArrayList<>();
		Set<String> existingCommentLikes = new HashSet<>(); // 중복 방지용 Set
		for (int i = 0; i < memberCommentCount; i++) {
			Member randomMember = members.get(faker.random().nextInt(members.size()));
			Comment randomComment = comments.get(faker.random().nextInt(comments.size()));

			String likeKey = randomMember.getId() + ":" + randomComment.getId();
			if (existingCommentLikes.contains(likeKey)) {
				continue;
			}
			existingCommentLikes.add(likeKey);

			MemberComment memberComment = MemberComment.builder()
				.member(randomMember)
				.comment(randomComment)
				.build();
			memberComments.add(memberComment);
		}
		memberCommentRepository.saveAll(memberComments);
	}
}