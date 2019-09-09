package com.example.demo.controller;

import com.example.demo.exception.CustomerException;
import com.example.demo.exception.CustomizeErrorCode;
import com.example.demo.mapper.QuestionMapper;
import com.example.demo.model.Question;
import com.example.demo.model.User;
import com.example.demo.service.NavigationService;
import com.example.demo.service.NotificationService;
import com.example.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NavigationService navigationService;

    @RequestMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id")long id,Model model,HttpServletRequest request){
        Question question = questionMapper.getById(id);
        User user = (User) request.getSession().getAttribute("user");
        if (user.getId()!=question.getCreator()){
            throw new CustomerException(CustomizeErrorCode.USR_WRONG_OPERATER);
        }
        if (question==null){
            throw new CustomerException(CustomizeErrorCode.PUBLISH_NOT_FOUND);
        }
        model.addAttribute("question",question);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("tag",question.getTag());
//        List<String> tags= Lists.newArrayList("java","spring boot","spring");
//        model.addAttribute("tags",tags);
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

//    @RequestMapping("publish")
//    public String test(
//            String title,
//            String description,
//            String tag
//    ){
//        System.out.println("第一个参数"+title+"第二个参数"+description+"第三个参数"+tag);
//        return "redirect:/";
//    }

    @RequestMapping("publish")
    public String doPublish(String title,
                            String description,
                            String tag,
                            @RequestParam(value ="id", required =false,defaultValue = "0") long id,
                            HttpServletRequest request,
                            Model model){
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        if (id != 0){
            question.setId(id);
        }
        Cookie[] cookies = request.getCookies();
        User user = (User) request.getSession().getAttribute("user");
        if (user==null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        question.setCreator(user.getId());
//        if (id!=0){
//            question.setId(id);
//        }
        model.addAttribute("title",title);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
