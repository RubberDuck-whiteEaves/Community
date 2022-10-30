package life.majiang.community.mapper;

import java.util.List;
import life.majiang.community.model.Comment;
import life.majiang.community.model.CommentExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface CommentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table COMMENT
     *
     * @mbg.generated Sun Oct 30 15:35:53 CST 2022
     */
    long countByExample(CommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table COMMENT
     *
     * @mbg.generated Sun Oct 30 15:35:53 CST 2022
     */
    int deleteByExample(CommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table COMMENT
     *
     * @mbg.generated Sun Oct 30 15:35:53 CST 2022
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table COMMENT
     *
     * @mbg.generated Sun Oct 30 15:35:53 CST 2022
     */
    int insert(Comment row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table COMMENT
     *
     * @mbg.generated Sun Oct 30 15:35:53 CST 2022
     */
    int insertSelective(Comment row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table COMMENT
     *
     * @mbg.generated Sun Oct 30 15:35:53 CST 2022
     */
    List<Comment> selectByExampleWithRowbounds(CommentExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table COMMENT
     *
     * @mbg.generated Sun Oct 30 15:35:53 CST 2022
     */
    List<Comment> selectByExample(CommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table COMMENT
     *
     * @mbg.generated Sun Oct 30 15:35:53 CST 2022
     */
    Comment selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table COMMENT
     *
     * @mbg.generated Sun Oct 30 15:35:53 CST 2022
     */
    int updateByExampleSelective(@Param("row") Comment row, @Param("example") CommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table COMMENT
     *
     * @mbg.generated Sun Oct 30 15:35:53 CST 2022
     */
    int updateByExample(@Param("row") Comment row, @Param("example") CommentExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table COMMENT
     *
     * @mbg.generated Sun Oct 30 15:35:53 CST 2022
     */
    int updateByPrimaryKeySelective(Comment row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table COMMENT
     *
     * @mbg.generated Sun Oct 30 15:35:53 CST 2022
     */
    int updateByPrimaryKey(Comment row);
}