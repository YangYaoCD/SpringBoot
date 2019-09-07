package com.example.demo.controller;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.enums.NotificationTypeEnum;
import com.example.demo.model.User;
import com.example.demo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @RequestMapping("notificaction/{id}")
    public String profile(@PathVariable(name = "id") long id, HttpServletRequest request){
        User user=(User)request.getSession().getAttribute("user");
        if (user==null){
            return "redirect:/";
        }
        NotificationDTO notificationDTO=notificationService.read(id,user);
        if (NotificationTypeEnum.REPLY_COMMENT.getType()==notificationDTO.getType()||NotificationTypeEnum.REPLY_QUESTION.getType()==notificationDTO.getType()){
            return "redirect:/question/"+notificationDTO.getOuterId();
        }else {
            return "redirect:/";
        }
    }
}
