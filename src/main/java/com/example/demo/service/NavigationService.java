package com.example.demo.service;

import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class NavigationService {
    @Autowired
    NotificationService notificationService;
    public Integer unreadCount(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if (user!=null){
            Integer totalCount =notificationService.UnreadCount(user.getId());
            return totalCount;
        }
        return 0;
    }
}
