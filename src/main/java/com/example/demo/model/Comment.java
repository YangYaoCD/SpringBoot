package com.example.demo.model;


import lombok.Data;

@Data
public class Comment {

  private long id;
  private long parentId;
  private long type;
  private long commentator;
  private long gmtCreate;
  private long gmtModified;
  private long likeCount;
  private String content;


}
