package life.majiang.community.mapper;

import life.majiang.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {
    @Insert("insert into user_info (account_id,name,token,gmt_create,gmt_modified) values (#{accountID},#{name},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
}