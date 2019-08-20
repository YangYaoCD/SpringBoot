package com.example.demo.service;

import com.example.demo.CommentTypeEnum;
import com.example.demo.dto.CommentDTO;
import com.example.demo.exception.CustomerException;
import com.example.demo.exception.CustomizeErrorCode;
import com.example.demo.mapper.CommentMapper;
import com.example.demo.mapper.QuestionMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Comment;
import com.example.demo.model.Question;
import com.example.demo.model.User;
import com.example.demo.model.UserExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Transactional//加事务
    public void insert(Comment comment) {
        if(comment.getParentId()==0){
            throw new CustomerException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType()==0&& CommentTypeEnum.isExist(comment.getType())){
            throw new CustomerException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType()==CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment=commentMapper.selectByprimary(comment.getParentId());
            if (dbComment==null){
                throw new CustomerException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        }else {
            //回复问题
            Question question = questionMapper.getById(comment.getParentId());
            if (question==null){
                throw new CustomerException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            questionMapper.incCommentCount(question);

        }
    }

    public List<CommentDTO> listByQuestionId(long id, CommentTypeEnum type) {
        /**
         * CTRL+ALT+P 抽取参数
         * CTRL+ALT+V 抽取变量
         * CTRL+ALT+M 抽取方法
         */
        List<Comment> comments=commentMapper.selectByParentIdAndType(id,type.getType());
        if (comments.size()==0){
            return new ArrayList<>();
        }
        //获取去重的评论人
        Set<Long> commentators = comments.stream().map(commentDTO -> commentDTO.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds=new ArrayList();
        userIds.addAll(commentators);

        //获取评论人并转换为Map
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换comment
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(commentDTO.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
