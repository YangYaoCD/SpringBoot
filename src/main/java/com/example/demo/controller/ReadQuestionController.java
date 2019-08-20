package com.example.demo.controller;

import com.example.demo.CommentTypeEnum;
import com.example.demo.dto.CommentCreateDTO;
import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.QuestionDTO;
import com.example.demo.mapper.QuestionMapper;
import com.example.demo.service.CommentService;
import com.example.demo.service.QuestionService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ReadQuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id",required = true) long id,
                           Model model){
        QuestionDTO questionDTO=questionService.getById(id);
        List<CommentDTO> commentDTOList=commentService.listByQuestionId(id, CommentTypeEnum.QUESTION);
        //累加阅读数
        questionService.incView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",commentDTOList);
        return "question";
    }
}
