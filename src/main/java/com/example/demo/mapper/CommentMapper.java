package com.example.demo.mapper;

import com.example.demo.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommentMapper {
//    insert into question(title,description,gmt_Create,gmt_Modified,creator,tag) values(#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})
    @Insert("insert into comment(parent_id,type,commentator,gmt_create,gmt_modified,like_count,content) values(#{parentId},#{type},#{commentator},#{gmtCreate},#{gmtModified},#{likeCount},#{content})")
    void insert(Comment comment);

    @Select("select * from comment where id=#{parentId}")
    Comment selectByprimary(long parentId);
}
