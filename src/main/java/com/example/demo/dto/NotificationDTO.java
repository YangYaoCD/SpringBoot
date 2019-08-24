package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private long id;
    private long gmtCreate;
    private Integer status;
    private long notifier;
    private String notifierName;
    private String outerTitle;
    private String type;
}
