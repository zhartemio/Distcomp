package com.bsuir.distcomp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequestTo {

    private Long id;

    @NotNull
    private Long topicId;

    @Size(min = 2, max = 2048)
    private String content;

}