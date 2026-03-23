package com.bsuir.distcomp.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("tbl_comment")
@Data
public class Comment {

    @PrimaryKey
    private CommentKey key;

    private String content;

}

