package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private long id;
    private long parentId;
    private Integer type;
    private long commentator;
    private long gmtCreate;
    private long getModified;
    private long likeCount;
    private String content;
    private User user;
    private long commentCount;
}
