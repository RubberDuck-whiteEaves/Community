package life.majiang.community.mapper;

import life.majiang.community.dto.QuestionQueryDTO;
import life.majiang.community.model.Question;
import life.majiang.community.model.QuestionExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface QuestionExtMapper {

    int incView(Question row);
    int incCommentCount(Question row);
    List<Question> selectRelated(Question question);
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);
    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}