package life.majiang.community.service;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user){
        // 判断user_info表中是否已存在这次请求登录的用户，不存在则插入，存在则更新
        User dbUser=userMapper.findByAccountID(user.getAccountID());
        if(dbUser==null){
            // 插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }
        else{
            // 更新
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarURL(user.getAvatarURL());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            userMapper.update(dbUser);
        }
    }
}
