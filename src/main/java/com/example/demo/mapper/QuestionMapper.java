package com.example.demo.mapper;

import com.example.demo.dto.CommentDTO;
import com.example.demo.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question(title,description,gmt_Create,gmt_Modified,creator,tag) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question ORDER BY gmt_Create DESC limit #{offset},#{size} ")
    List<Question> List(@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator=${userId} limit #{offset},#{size}")
    List<Question> ListById(@Param(value = "userId")long userId,@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);

    @Select("select count(1) from question where creator=${userId}")
    Integer countById(long userId);

    @Select("select * from question where id=#{id}")
    Question getById(Long id);

    @Update("update question set title=#{title},description=#{description},gmt_modified=#{gmtModified},tag=#{tag} where id= #{id}")
    Integer update(Question question);

    @Update("update question set view_count=view_count+1 where id=#{id}")
    void updateviewCount(@Param(value = "id")long id);

    @Update("update question set comment_count=comment_count+1 where id=#{id}")
    int incCommentCount(Question record);

    @Select("select * from question where tag REGEXP #{tagR} AND id!=#{id}")
    List<Question> selectRelated(String tagR,long id);
}
