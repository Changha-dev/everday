package com.toy.everyday.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toy.everyday.config.DataInitializerService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/data")
public class DataInitializerController {

	private final DataInitializerService dataInitializerService;

	@PostMapping("/generate")
	public ResponseEntity<String> generateData(
		@RequestParam(defaultValue = "100") int members,
		@RequestParam(defaultValue = "1000") int posts,
		@RequestParam(defaultValue = "5000") int comments,
		@RequestParam(defaultValue = "20000") int memberPosts,
		@RequestParam(defaultValue = "10000") int memberComments
	) {
		dataInitializerService.generateData(members, posts, comments, memberPosts, memberComments);
		String message = String.format("데이터 생성 완료: Members=%d, Posts=%d, Comments=%d, PostLikes=%d, CommentLikes=%d",
			members, posts, comments, memberPosts, memberComments);
		return ResponseEntity.ok(message);
	}
}