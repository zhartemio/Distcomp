package com.bsuir.distcomp.dto;

import lombok.Data;

@Data
public class CommentResponseTo {

    private Long id;
    private Long topicId;
    private String content;

}