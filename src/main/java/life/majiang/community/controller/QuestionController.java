package life.majiang.community.controller;

import life.majiang.community.dto.CommentCreateDTO;
import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.QuestionDTO;
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

        List<CommentDTO> comments=commentService.listByQuestionId(id);
        // 累加阅读数
        questionService.incView(id);
        questionDTO.setViewCount(questionDTO.getViewCount()+1);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        return "question";
    }
}
