package life.majiang.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 拦截器是面向切面（AOP）编程中应用的一种统一处理方案，就是在你的Controller、Servie或者一个Method调用一个Method，或者在Method调用一个Method之后，统一的进行处理的方案，基于Java的反射机制。
// 拦截器，在AOP（Aspect-Oriented Programming）中可以用于在某个方法或者字段被访问之前，进行拦截，然后在之前或者之后加入某些统一的处理方法。拦截是AOP的一种具象的实现方式。
// Spring MVC 中的Interceptor拦截请求是通过HandlerInterceptor来实现的。
@Configuration
// 如果要想使用Spring MVC Auto-configuration的特性并使用拦截器时，需要在自己实现的WebMvcConfigurer类上注解@Configuration而不使用@EnableWebMvc注解
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private SessionInterceptor sessionInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // "/**"的意思是所有文件夹及里面的子文件夹
        // "/*"是所有文件夹，不含子文件夹
        // "/"是web项目的根目录
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");//拦截所有路径
        // .excludePathPatterns("/login", "/", "/exit", "/get_cpacha")//排除路径
        // .excludePathPatterns("/xadmin/**");//排除静态资源拦截
    }
}
