package com.bsuir.distcomp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "tbl_writer" )
@Data
public class Writer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String login;

    private String password;

    private String firstname;

    private String lastname;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Topic> topics;
}

