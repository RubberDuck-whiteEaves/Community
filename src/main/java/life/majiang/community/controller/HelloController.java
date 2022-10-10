package life.majiang.community.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Controller:允许当前的类去接收前端的请求
@Controller
public class HelloController {
    // url中指定的hello路径使用该hello方法来处理
    @GetMapping("/hello")
    // url传参：域名?参数名=参数值
    // @RequestParam中的name="name"指定参数名为name
    // 形参列表中的name用于接收参数名为name的参数值

    // 返回值：不管返回什么类型，最终都会被转化成ModeAndView返回！
    // 1.使用String作为请求处理方法的返回值类型是比较通用的方法，这样返回的逻辑视图名不会和请求URL绑定，具有很大的灵活性，而模型数据又可以通过ModelMap控制
    // 2.使用void,Map,Model时，返回对应的逻辑视图名称真实url为：prefix前缀+方法请求url+suffix后缀组成。(表示无需跳转到其它任何资源，则url中指定了哪个路径则必须存在相应的资源文件，如此处则必须存在hello.html文件)
    // 3.使用String,ModelAndView返回视图名称可以不受请求的url绑定，ModelAndView可以设置返回的视图名称。

    public String hello(@RequestParam(name = "name") String name, Model model){// 按command+p可以提示需要传入的参数
        // 把数据传递给页面
        model.addAttribute("name",name);
        return "hi";// 返回字符串指定逻辑视图名，通过视图解析器解析为物理视图地址，即/templates/hi.html，这样就与url中指定的hello文件资源名不同了，即可以不受请求的url绑定
    }
}
