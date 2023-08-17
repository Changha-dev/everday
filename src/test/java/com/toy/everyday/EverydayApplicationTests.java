package com.toy.everyday;

import com.toy.everyday.entity.Post;
import com.toy.everyday.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class EverydayApplicationTests {

	@Autowired
	private PostRepository postRepository;
	@Test
	void testJpa() {
		for(int i=1 ; i <= 300; i++){
			String subject = String.format("테스트 데이터입니다: [%03d]", i);
			String content = "내용무";

			this.postRepository.save(Post.builder()
					.subject(subject)
					.content(content)
					.createDate(LocalDateTime.now())
					.build());
		}
	}

}
