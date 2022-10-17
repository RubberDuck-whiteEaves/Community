package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.User;
import life.majiang.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    /*这个从IoC容器中获取的实例与main中获取的是同一个吗？*/
    private GithubProvider githubProvider;

    // @Value注解的作用是将配置文件中指定key的value读出来（将配置文件中的key value读到一个map中，等到使用时通过注解获取，赋值到该变量中），赋值给指定被注解的变量
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectURI;
    
    @Autowired
    private UserMapper userMapper;

    @GetMapping("callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletRequest request){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectURI);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null){
            User user = new User();
            // UUID:通用唯一识别码
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUser.getName());
            user.setAccountID(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            // 登陆成功，写cookie和session
            request.getSession().setAttribute("user",githubUser);
            System.out.println(githubUser);
            // 希望登陆成功以后返回index页面
            // 这里不能写redirect:index，或是写redirect:index.html
            // 因为static文件夹以及templates文件夹是受保护的，也就是说这两个文件夹下的文件是无法通过url直接访问的，以至于在springmvc下使用重定向会报404
            // 那么如何能够直接访问到html或者css呢？（这里尝试了public方法，仍然无法访问）
            // 方法一:我们可以在resources下创建一个名为public的文件夹，顾名思义，放在此文件夹下的文件是共有的，可以直接通过url访问，当然也可以springmvc重定向访问啦。
            // 方法二:在main下创建webapp文件夹，将文件放到此文件夹下，效果同上。
            // 由于尝试了方法一失败了，所以这里采取其他方式来做，即通过重定向到另一个Controller，注解了@GetMapping("/")的方法中，再通过那个方法指定的逻辑视图index解析为物理视图地址/templates/index.html
            return "redirect:/";
        }
        else{
            // 登陆失败，重新登陆
            return "redirect:/";
        }
    }
}
