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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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

    // 用户若在github中授权登陆，则会重定向回一个我们之前传给github的url：http://localhost:8887/callback
    // 则需要一个注解了callback的函数来处理
    // 重定向回我们的url的同时，还会传回code和state两个参数，我们接收这两个参数，并加上其他的一些参数，作为下一步对github发出POST请求的参数列表
    // Post参数超过两个以上的话，将这些参数包装为AccessTokenDTO，调用方法获取accessToken
    // 调用方法发送accessToken获取githubUser
    // 如果githubUser非空，将user写入
    // 如果githubUser为空，则登陆失败
    @GetMapping("callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectURI);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null){
            // 获取授权成功
            User user = new User();
            // UUID:通用唯一识别码
            // 利用UUID生成token，用于唯一标识user
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountID(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            // 插入数据库的过程，相当于写入了session，即把"session"持久化了（用DB对实物的存储，代替了session的写入），则后续不必在写入session了
            userMapper.insert(user);
            // 这里光使用session来验证登陆是不可取的：
            // 说明一下session可以实现的效果，以及无法完成的效果：
            // 可以实现的效果：
            // 1 在服务器未断开时，点击一次登录，则后续在同一个浏览器按刷新，或者反复访问index首页，都不会再显示登录，而会显示个人信息
            // 2 用户登出，前端重新显示登录按钮，然后在同一个浏览器中使用其他用户登录，可以正确地显示其他用户的个人信息
            //   虽然默认情况下，一个浏览器独占一个session对象（因为默认用来保存session的cookie在该域名下都有效，也就是说不论你访问什么资源，这个cookie ID都会被传送到服务器端，来获取"本浏览器"的session）
            //   但是在获取到新的githubUser时，仍然会通过getSession().setAttribute("user",githubUser)对Session中的用户进行覆盖，因此可以正确显示下一个登录用户的个人信息
            // 无法完成的效果：
            // 1 服务器断开时，重启服务器（重启应用），用户需要重新登录（因为session是存储在这个application的内存中的，如果应用重启，则session消失），而实际上用户最好是感受不到服务器重启了，最好是不需重新登录
            //   用token存储在db中，再通过cookie传输，可以做到服务器重启，用户也不需要重新登录（只需要浏览器中的cookie未被清除即可）
            response.addCookie(new Cookie("token",token));
            /*// 登陆成功，写cookie和session
            // request.getSession(true)：返回当前request中的HttpSession，若为null则新建一个HttpSession
            // request.getSession(false)：返回当前request中的HttpSession，若为null则返回null
            // request.getSession()等价于request.getSession(true)
            // 在cookie未被禁用的情况下，通过getSession(false)来获取HttpSession，"必然"（未完全调研清楚）能返回HttpSession
            // 不考虑特殊情况，既然每次请求都包含HttpSession，则在Http请求报文中没必要通过特别的修改来告知服务器："这次请求需要帮我维系一个session，你的响应报文中set cookie应该包含一个cookie来维系该session"
            // 反之，服务器拥有主动权来决定是否set cookie来维系该session，而服务器的响应中是否包含set cookie，则取决于服务器的代码中，是否调用了request.getSession()
            // 若调用了，则服务器直接获取当前request中的HttpSession，并为之生成一个session id，在响应中进行回传
            // （判断当前request中是否有HttpSession，有没有可能是通过session id判断的，如果当前客户端请求中的所有cookie的value中，服务器都没有session id与之对应，则当前request中没有HttpSession，如果有，则当前request中有HttpSession，
            // 具体是不是这样判断的，可在后续代码中看利用session时是否需要程序员自己写代码逻辑来判断传入的所有cookie，服务器有没有session id与之对应，如果这部分代码逻辑需要程序员实现，那么getSession()中显然不包含这些逻辑，不过如果不需要，也无法证明getSession()中一定包含这些逻辑）
            request.getSession().setAttribute("user",githubUser);*/

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
