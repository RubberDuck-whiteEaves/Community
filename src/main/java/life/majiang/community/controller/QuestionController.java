package life.majiang.community.controller;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.service.CommentService;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {
    // Service层通过调用Mapper层的方法获取Model数据，再在Service层进行组装，获得DTO数据，在Controller层使用
    // 同时，Provider层也可以用于处理DTO数据，因此后续可以考虑将Service层获取的和Provider层获取的两种DTO数据分开
    // 因此，若想要在Controller层对DTO数据进行处理，应该想到使用Provider层或者Service层
    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model){
        QuestionDTO questionDTO=questionService.getById(id);
        List<QuestionDTO> relatedQuestions=questionService.selectRelated(questionDTO);

        List<CommentDTO> comments=commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        // 累加阅读数
        questionService.incView(id);
        questionDTO.setViewCount(questionDTO.getViewCount()+1);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestions);
        // 对问题发送评论的post请求不会在这里处理，所以这里的return "question"与发送post请求后页面刷新无关
        return "question";
    }
}
