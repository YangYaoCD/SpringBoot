package com.example.demo.controller;

import com.example.demo.dto.QuestionPageDTO;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.User;
import com.example.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {
    @Autowired
    QuestionService questionService;
    @Autowired
    UserMapper userMapper;
    @GetMapping("profile/{action}")
    public String profile(@PathVariable(name = "action")String action, Model model, HttpServletRequest request,
                          @RequestParam(name = "currentPage", defaultValue = "1",required = false)Integer currentPage,
                          @RequestParam(name = "size",defaultValue = "3",required = false) Integer size){
        if (action.equals("questions")){
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的提问");
        }else if (action.equals("replies")){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }else {
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的提问");
        }
        User user = (User) request.getSession().getAttribute("user");
        QuestionPageDTO questionPageDTO = questionService.list(user.getId(), currentPage, size);
        model.addAttribute("question",questionPageDTO);
        return "profile";

    }
}
