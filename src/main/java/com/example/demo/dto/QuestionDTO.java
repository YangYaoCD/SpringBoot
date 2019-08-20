package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.Data;

@Data
public class QuestionDTO {
    private long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
