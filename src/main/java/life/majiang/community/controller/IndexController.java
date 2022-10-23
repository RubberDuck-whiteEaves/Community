package life.majiang.community.controller;

import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model){
        // 首页中的逻辑：获取cookie中的token，在数据库中查询token是否存在，如果存在，则已经登陆成功，不会每次刷新首页都要求重新登陆
        // 同时将user写入session，使得前端可以通过session来决定是展示个人信息还是登陆
        // 设置cookie是服务器通过response设置的，获取cookie是在浏览器发送给服务器的request请求中获取的
        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length!=0){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("token")){
                    String token=cookie.getValue();
                    User user=userMapper.findByToken(token);
                    if(user!=null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        List<QuestionDTO> questionDTOList=questionService.list();
        for (QuestionDTO questionDTO : questionDTOList) {
            questionDTO.setDescription("人生充满遗憾，失败贯穿始终");
        }
        model.addAttribute("questions",questionDTOList);
        return "index";
    }
}
