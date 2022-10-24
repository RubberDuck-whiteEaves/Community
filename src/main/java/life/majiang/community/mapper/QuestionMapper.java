package life.majiang.community.mapper;

import life.majiang.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QuestionMapper {
    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    // #{}:当形参为多个值，且#{}中写入形参名直接传递时，形参列表中需要额外加@Param注解
    // #{}:当形参为多个值，且#{}中写入形参的index值间接传递时，形参列表中不需要额外加@Param注解
    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(@Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(1) from question")
    Integer count();

    @Select("select * from question where creator = #{userID} limit #{offset},#{size}")
    List<Question> listByUserID(@Param("userID") Integer userID, @Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(1) from question where creator = #{userID}")
    Integer countByUserID(Integer userID);
}
