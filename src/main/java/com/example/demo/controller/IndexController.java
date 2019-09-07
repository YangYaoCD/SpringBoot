package com.example.demo.controller;

import com.example.demo.dto.PaginationDTO;
import com.example.demo.dto.QuestionDTO;
import com.example.demo.model.User;
import com.example.demo.service.NavigationService;
import com.example.demo.service.NotificationService;
import com.example.demo.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(name = "currentPage",defaultValue = "1",required = false) Integer currentPage,
                        @RequestParam(name = "size",defaultValue = "7",required = false) Integer size,
                        @RequestParam(name = "search",required =false ) String search){

        PaginationDTO<QuestionDTO> questionPageDTO=questionService.List(search,currentPage,size);
        if (StringUtils.isNotBlank(search)){
            request.getSession().setAttribute("search",search);
        }
        notificationService.setReplyCount(request, model);
        model.addAttribute("questions",questionPageDTO);
        return "index";
    }


}
