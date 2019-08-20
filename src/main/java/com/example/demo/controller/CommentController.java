package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.demo.CommentTypeEnum;
import com.example.demo.dto.CommentCreateDTO;
import com.example.demo.dto.CommentDTO;
import com.example.demo.dto.ResultDTO;
import com.example.demo.exception.CustomerException;
import com.example.demo.exception.CustomizeErrorCode;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.model.Comment;
import com.example.demo.model.User;
import com.example.demo.service.CommentService;
import org.h2.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@RequestMapping("comment")
public class CommentController {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentService commentService;

    @RequestMapping("post")
    public @ResponseBody
    ResultDTO post(@RequestBody CommentCreateDTO commentCreateDTO, HttpServletRequest request, HttpServletResponse response){
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(commentCreateDTO.toString());
        if(user==null){
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        if (commentCreateDTO==null|| StringUtils.isNullOrEmpty(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0);
        commentService.insert(comment);
        ResultDTO resultDTO = ResultDTO.okOf();
        return resultDTO;
    }

    @ResponseBody
    @RequestMapping("/{id}")
    public ResultDTO<List> comments(@PathVariable(name = "id") long id){
        List<CommentDTO> commentDTOList = commentService.listByQuestionId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOList);
    }
}
