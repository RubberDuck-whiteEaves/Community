package life.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

// @Controller注解是把当前类作为路由api的承载者
// @Component注解是将该类作为Bean装配到IoC容器中
@Component
// 希望GithubProvider提供Github第三方支持的能力
public class GithubProvider {
    // 通过AccessTokenDTO作为请求参数获取AccessToken
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string=response.body().string();
            String token = string.split("&")[0].split("=")[1];
            System.out.println(string);
            return token;
        } catch (Exception e) {
            // 防止获取到的string格式无法进行上述的split而出错
            System.out.println(99999999);
            e.printStackTrace();
        }
        return null;
    }

    // 通过AccessToken作为请求参数，获取GithubUser
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        // 使用这种方式.url("https://api.github.com/user?aaccess_token="+accessToken)会报错，返回401，即要求填入Authorization字段
        // OAuth2.0是一个关于授权的开放网络协议。
        // 该协议在第三方应用与服务提供平台之间设置了一个授权层。第三方应用需要服务资源时，并不是直接使用用户帐号密码登录服务提供平台，
        // 而是通过服务提供平台的授权层获取token令牌，用户可以在授权时指定token的权限范围和有效期。第三方应用获取到token以后，才可以访问用户资源。
        // 在使用JSON Web Token（JWT）作为单点登录的验证媒介时，为保证安全性，建议将JWT的信息存放在HTTP的请求头中，通常是Authorization字段，并使用https对请求链接进行加密传输
        // 所以这里必须通过Authorization字段传输token，指定HTTP get请求消息头中的Authorization字段的值为"Bearer accessToken"才能成功获取user信息
        Request request = new Request.Builder()
                .url("https://api.github.com/user").header("Authorization","Bearer "+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string=response.body().string();
            // 将Json格式的String转化为一个java的类对象
            // 在JSON.parseObject的时候，会去填充名称相同的属性。
            // 对于Json字符串中没有，而类有的属性，会为null；
            // 对于类没有，而Json字符串有的属性，不做任何处理，类不可能凭空出现一个属性来接收，使用时也不会报错。
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
        }
        return null;
    }
}
