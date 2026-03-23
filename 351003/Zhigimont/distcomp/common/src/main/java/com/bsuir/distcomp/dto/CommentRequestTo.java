package com.bsuir.distcomp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequestTo {
    private Long topicId;
    private Long id;
    @NotBlank
    @Size(min = 2, max = 2048)
    private String content;

}