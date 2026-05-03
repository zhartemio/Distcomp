package com.bsuir.distcomp.service;

import com.bsuir.distcomp.dto.CommentListResponseTo;
import com.bsuir.distcomp.dto.CommentResponseTo;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ResponseHolder {

    private final Map<String, CompletableFuture<CommentResponseTo>> map = new ConcurrentHashMap<>();
    private final Map<String, CompletableFuture<CommentListResponseTo>> mapL = new ConcurrentHashMap<>();

    public CompletableFuture<CommentResponseTo> create(String id) {
        CompletableFuture<CommentResponseTo> future = new CompletableFuture<>();
        map.put(id, future);
        return future;
    }

    public void complete(String id, CommentResponseTo response) {
        CompletableFuture<CommentResponseTo> future = map.remove(id);
        if (future != null) {
            future.complete(response);
        }
    }

    public void complete(String id, CommentListResponseTo response) {
        CompletableFuture<CommentListResponseTo> future = mapL.remove(id);
        if (future != null) {
            future.complete(response);
        }
    }
}
