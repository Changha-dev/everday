package com.toy.everyday.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {

    @NotEmpty(message = "댓글이 비어있습니다.")
    private String content;
}
