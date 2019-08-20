package com.example.demo.service;


import ch.qos.logback.core.db.dialect.DBUtil;
import com.example.demo.dto.QuestionDTO;
import com.example.demo.dto.QuestionPageDTO;
import com.example.demo.exception.CustomerException;
import com.example.demo.exception.CustomizeErrorCode;
import com.example.demo.mapper.QuestionMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Question;
import com.example.demo.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//组装user和question时的中间层
@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    public QuestionPageDTO List(Integer currentPage, Integer size) {
        QuestionPageDTO questionPageDTO = new QuestionPageDTO();
        //先获取记录总数
        Integer totalCount = questionMapper.count();
        //计算并设置总页数
        questionPageDTO.setTotalPage(totalCount,size);
        //计算并设置当前页的页码
        questionPageDTO.setCurrentPage(currentPage,questionPageDTO.getTotalPage());
        Integer offset=size*(questionPageDTO.getCurrentPage()-1);
        List<Question> questionList=questionMapper.List(offset,size);
        List<QuestionDTO> questionDTOS=new ArrayList<QuestionDTO>();
        for (Question question:questionList
             ) {
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        questionPageDTO.setQuestionDTOS(questionDTOS);
        questionPageDTO.setShowPrevious(questionPageDTO.getCurrentPage());
        questionPageDTO.setShowFirstPage(questionPageDTO.getCurrentPage());
        questionPageDTO.setShowNext(questionPageDTO.getCurrentPage(),questionPageDTO.getTotalPage());
        questionPageDTO.setShowEndPage(questionPageDTO.getCurrentPage(),questionPageDTO.getTotalPage());
        questionPageDTO.setPage(questionPageDTO.getShowListSize(),questionPageDTO.getCurrentPage(),questionPageDTO.getTotalPage());
        return questionPageDTO;

    }

    public QuestionPageDTO list(long userId, Integer currentPage, Integer size) {
        QuestionPageDTO questionPageDTO = new QuestionPageDTO();
        //先获取记录总数
        Integer totalCount = questionMapper.countById(userId);
        //计算并设置总页数
        questionPageDTO.setTotalPage(totalCount,size);
        //计算并设置当前页的页码
        questionPageDTO.setCurrentPage(currentPage,questionPageDTO.getTotalPage());
        Integer offset=size*(questionPageDTO.getCurrentPage()-1);
        List<Question> questionList=questionMapper.ListById(userId,offset,size);
        List<QuestionDTO> questionDTOS=new ArrayList<QuestionDTO>();
        for (Question question:questionList) {
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        questionPageDTO.setQuestionDTOS(questionDTOS);
        questionPageDTO.setShowPrevious(questionPageDTO.getCurrentPage());
        questionPageDTO.setShowFirstPage(questionPageDTO.getCurrentPage());
        questionPageDTO.setShowNext(questionPageDTO.getCurrentPage(),questionPageDTO.getTotalPage());
        questionPageDTO.setShowEndPage(questionPageDTO.getCurrentPage(),questionPageDTO.getTotalPage());
        questionPageDTO.setPage(questionPageDTO.getShowListSize(),questionPageDTO.getCurrentPage(),questionPageDTO.getTotalPage());
        return questionPageDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question=questionMapper.getById(id);
        if (question==null){
            throw new CustomerException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user=userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId()==null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            questionMapper.create(question);
        }else {
            question.setGmtModified(question.getGmtCreate());
            Integer update = questionMapper.update(question);
            if (update!=1){
                throw new CustomerException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(long id) {
        questionMapper.updateviewCount(id);
    }
}
