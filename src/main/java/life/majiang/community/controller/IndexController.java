package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    // page:当前页码
    // size:一页size条question
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name="page",defaultValue = "0") Integer page,
                        @RequestParam(name="size",defaultValue = "6") Integer size,
                        @RequestParam(name="search",required = false) String search){
        // 首页中的逻辑：获取cookie中的token，在数据库中查询token是否存在，如果存在，则已经登陆成功，不会每次刷新首页都要求重新登陆
        // 同时将user写入session，使得前端可以通过session来决定是展示个人信息还是登陆
        // 设置cookie是服务器通过response设置的，获取cookie是在浏览器发送给服务器的request请求中获取的
//        以下代码用拦截器实现
//        Cookie[] cookies = request.getCookies();
//        if(cookies!=null&&cookies.length!=0){
//            for(Cookie cookie:cookies){
//                if(cookie.getName().equals("token")){
//                    String token=cookie.getValue();
//                    User user=userMapper.findByToken(token);
//                    if(user!=null){
//                        request.getSession().setAttribute("user",user);
//                    }
//                    break;
//                }
//            }
//        }

        PaginationDTO pagination=questionService.list(search,page,size);
        model.addAttribute("pagination",pagination);
        model.addAttribute("search", search);
        return "index";
    }
}
