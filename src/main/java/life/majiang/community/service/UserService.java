package life.majiang.community.service;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user){
        // 判断user_info表中是否已存在这次请求登录的用户，不存在则插入，存在则更新
//        User dbUser=userMapper.findByAccountId(user.getAccountId());
        UserExample userExample=new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size()==0){
            // 插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }
        else{
            // 更新
//            这里不能在users.get(0)取了之后再更新里面的字段，用userMapper.updateByPrimaryKeySelective(dbUser);写进去
//            因为updateByPrimaryKeySelective的方法是写所有非空字段，GmtCreate这种不必更新的字段也会由于非空而更新
//            User dbUser=users.get(0);
//            dbUser.setGmtModified(System.currentTimeMillis());
//            dbUser.setAvatarUrl(user.getAvatarUrl());
//            dbUser.setName(user.getName());
//            dbUser.setToken(user.getToken());
            User updateUser=new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            userMapper.updateByPrimaryKeySelective(updateUser);
        }
    }
}
