package com.example.demo.controller;

import com.example.demo.dto.QuestionDTO;
import com.example.demo.dto.QuestionPageDTO;
import com.example.demo.mapper.QuestionMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Question;
import com.example.demo.model.User;
import com.example.demo.service.QuestionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String index(HttpServletRequest request, Model model, @RequestParam(name = "currentPage",defaultValue = "1",required = false) Integer currentPage,@RequestParam(name = "size",defaultValue = "7",required = false) Integer size){

        QuestionPageDTO questionPageDTO=questionService.List(currentPage,size);
        model.addAttribute("questions",questionPageDTO);
        return "index";
    }
}
