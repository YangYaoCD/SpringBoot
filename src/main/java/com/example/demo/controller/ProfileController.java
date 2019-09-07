package com.example.demo.controller;

import com.example.demo.dto.PaginationDTO;
import com.example.demo.dto.QuestionDTO;
import com.example.demo.mapper.NotificationMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.NotificationExample;
import com.example.demo.model.User;
import com.example.demo.service.NotificationService;
import com.example.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    QuestionService questionService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationMapper notificationMapper;

    @GetMapping("profile/{action}")
    public String profile(@PathVariable(name = "action")String action, Model model, HttpServletRequest request,
                          @RequestParam(name = "currentPage", defaultValue = "1",required = false)Integer currentPage,
                          @RequestParam(name = "size",defaultValue = "3",required = false) Integer size){
        User user = (User) request.getSession().getAttribute("user");
        if (action.equals("questions")){
            model.addAttribute("section","question");
            questionListUtil(model, currentPage, size, user);
        }else if (action.equals("replies")){
            PaginationDTO paginationDTO=notificationService.list(user.getId(),currentPage,size);
            NotificationExample notificationExample = new NotificationExample();
            notificationExample.createCriteria().andReceiverEqualTo(user.getId());
            model.addAttribute("repliesPageDTO",paginationDTO);
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }else {
            model.addAttribute("section","question");
            questionListUtil(model, currentPage, size, user);

        }
        return "profile";

    }

    private void setUnreadCount(Model model, User user) {
        Integer totalCount = notificationService.UnreadCount(user.getId());
        model.addAttribute("unreadCount", totalCount);
    }

    private void questionListUtil(Model model, @RequestParam(name = "currentPage", defaultValue = "1", required = false) Integer currentPage, @RequestParam(name = "size", defaultValue = "3", required = false) Integer size, User user) {
        model.addAttribute("sectionName", "我的提问");
        PaginationDTO<QuestionDTO> questionPageDTO = questionService.list(user.getId(), currentPage, size);
        model.addAttribute("question", questionPageDTO);
    }
}
