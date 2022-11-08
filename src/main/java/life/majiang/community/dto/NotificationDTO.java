package life.majiang.community.dto;

import life.majiang.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    // 通知者的id
    private Long notifier;
    // 通知者的名字
    private String notifierName;
    // 最外层问题的title
    private String outerTitle;
    // 最外层问题的的id
    private Long outerid;
    // 评论的是"问题"or"评论"
    private String typeName;
    // 评论的是问题or评论
    private Integer type;
}
