package com.bsuir.distcomp.service;

import com.bsuir.distcomp.dto.CommentRequestTo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    private CommentService service;

    @Test
    void create_shouldWork() {
        var comment = service.create(new CommentRequestTo());
        assertNotNull(comment);
    }
}
