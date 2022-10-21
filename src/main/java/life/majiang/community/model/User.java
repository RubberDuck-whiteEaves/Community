package life.majiang.community.model;

import lombok.Data;

@Data
public class User {
    /*必须为Integer吗，int不可以吗*/
    private Integer id;
    private String accountID;
    private String name;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarURL;
}
