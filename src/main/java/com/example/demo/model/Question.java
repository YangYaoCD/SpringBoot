package com.example.demo.model;

import lombok.Data;

@Data
public class Question {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
}
