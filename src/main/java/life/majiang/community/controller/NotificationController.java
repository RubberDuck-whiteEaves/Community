package life.majiang.community.controller;

import life.majiang.community.dto.NotificationDTO;
import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.enums.NotificationTypeEnum;
import life.majiang.community.model.User;
import life.majiang.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/notification/{id}")
    // @PathVariable用于接收@GetMapping中的变量，其中name参数和value参数完全等价，都可以用于指定@GetMapping中的变量名
    public String profile(HttpServletRequest request,
                          @PathVariable(name="id") Long id,
                          Model model){
        User user = (User) request.getSession().getAttribute("user");

        if(user==null){
            model.addAttribute("error","用户未登录");
            return "redirect:/";
        }
        NotificationDTO notificationDTO=notificationService.read(id,user);
        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType()
                || NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()) {
            return "redirect:/question/" + notificationDTO.getOuterid();
        } else {
            return "redirect:/";
        }
    }
}
