package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

// Controller：用于接收前端发送的各种请求，指定各种方法进行处理（可以使用model.addAttribute等方法完成前后端的连接），返回html响应并指定路由到的页面
@Controller
public class ProfileController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    // @PathVariable用于接收@GetMapping中的变量，其中name参数和value参数完全等价，都可以用于指定@GetMapping中的变量名
    public String profile(HttpServletRequest request,
                          @PathVariable(name="action") String action,
                          Model model,
                          @RequestParam(name="page",defaultValue = "1") Integer page,
                          @RequestParam(name="size",defaultValue = "2") Integer size){
        User user=null;

        Cookie[] cookies = request.getCookies();
        if(cookies!=null&&cookies.length!=0){
            for(Cookie cookie:cookies){
                if(cookie.getName().equals("token")){
                    String token=cookie.getValue();
                    user=userMapper.findByToken(token);
                    if(user!=null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
        if(user==null){
            model.addAttribute("error","用户未登录");
            return "redirect:/";
        }
        if (action.equals("questions")) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
//            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
//            model.addAttribute("pagination", paginationDTO);
        }
        else if(action.equals("replies")){
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
        }
        PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
        model.addAttribute("pagination",paginationDTO);
        return "profile";
    }
}
