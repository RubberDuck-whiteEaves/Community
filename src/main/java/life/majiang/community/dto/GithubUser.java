package life.majiang.community.dto;

import lombok.Data;

// 在github第三方登陆中，获取的最终用户信息
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarURL;
}
