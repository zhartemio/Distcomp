package com.bsuir.distcomp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_comment")
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
}

