package life.majiang.community.controller;

import life.majiang.community.dto.CommentCreateDTO;
import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.model.Comment;
import life.majiang.community.model.User;
import life.majiang.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 通过RequestBody可以接收前端发送的json格式的数据，且自动转化为对象
    // 通过ResponseBody可以把对象自动序列化成json，响应给前端
    // 这就是一种最基本的前后端分离
    // 这个方法的作用是收到前端发送的post方法，并将其插入数据库
    // 这里也是采用了ajax的方式，对页面进行局部的更新而不需要刷新页面
    @ResponseBody
    @RequestMapping(value="/comment", method = RequestMethod.POST)
    public ResultDTO post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user==null){
            // 由于不想用CustomizeExceptionHandler或者CustomizeErrorController跳转到error页（白页）处理
            // 这里则新建了一个ResultDTO类返回给前端一个json进行响应，让前端对这个错误进行响应的展示和处理
/*
            这里后续CustomizeExceptionHandler集成了返回json的功能，这里是不是可以修改为throw异常？
*/
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if(commentCreateDTO==null|| StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    // 这个方法的作用是收到前端发送的get方法，并展示评论的二级评论（对应于QuestionController中的的question方法，用于展示问题的评论）
    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id") Long id) {
        List<CommentDTO> comments=commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(comments);
    }
}
