package com.example.demo.service;


import com.example.demo.dto.PaginationDTO;
import com.example.demo.dto.QuestionDTO;
import com.example.demo.dto.QuestionQueryDTO;
import com.example.demo.exception.CustomerException;
import com.example.demo.exception.CustomizeErrorCode;
import com.example.demo.mapper.QuestionMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Question;
import com.example.demo.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//组装user和question时的中间层
@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;
    public PaginationDTO<QuestionDTO> List(String search,Integer currentPage, Integer size) {

        if (StringUtils.isNotBlank(search)){
            String[] tags = StringUtils.split(search, " ");
            search=Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        PaginationDTO<QuestionDTO> questionPageDTO = new PaginationDTO<>();
        //先获取记录总数
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        Integer totalCount;
        if (search==null){
            totalCount = questionMapper.count();
        }else {
            totalCount = questionMapper.countBySearch(questionQueryDTO);
        }
        //计算并设置总页数
        questionPageDTO.setTotalPage(totalCount,size);
        //计算并设置当前页的页码
        questionPageDTO.setCurrentPage(currentPage,questionPageDTO.getTotalPage());
        Integer offset=size*(questionPageDTO.getCurrentPage()-1);
        List<Question> questionList;
        if (search==null){
            questionList=questionMapper.List(offset,size);
        }else {
            questionList=questionMapper.ListBySearch(search,offset,size);
        }
        List<QuestionDTO> questionDTOS=new ArrayList<QuestionDTO>();
        for (Question question:questionList
             ) {
            User user=userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        questionPageDTO.setLists(questionDTOS);
        questionPageDTO.setShowPrevious(questionPageDTO.getCurrentPage());
        questionPageDTO.setShowFirstPage(questionPageDTO.getCurrentPage());
        questionPageDTO.setShowNext(questionPageDTO.getCurrentPage(),questionPageDTO.getTotalPage());
        questionPageDTO.setShowEndPage(questionPageDTO.getCurrentPage(),questionPageDTO.getTotalPage());
        questionPageDTO.setPage(questionPageDTO.getShowListSize(),questionPageDTO.getCurrentPage(),questionPageDTO.getTotalPage());
        return questionPageDTO;

    }

    public PaginationDTO<QuestionDTO> list(long userId, Integer currentPage, Integer size) {
        PaginationDTO<QuestionDTO> questionPageDTO = new PaginationDTO<>();
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
        questionPageDTO.setLists(questionDTOS);
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
        if (null==question.getId()){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setLikeCount(0);
            questionMapper.create(question);
        }else {
            question.setGmtModified(System.currentTimeMillis());
            Integer update = questionMapper.update(question);
            if (update!=1){
                throw new CustomerException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(long id) {
        questionMapper.updateviewCount(id);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        String tagString = queryDTO.getTag();
        if (StringUtils.isBlank(tagString)){
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(tagString, ",");
        String tagStr = Arrays.stream(tags).collect(Collectors.joining("|"));
        System.out.println(tagStr);
        List<Question> selectRelatedList = questionMapper.selectRelated(tagStr, queryDTO.getId());
        List<QuestionDTO> questionDTOList = selectRelatedList.stream().map(question -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());

        return questionDTOList;
    }
}
