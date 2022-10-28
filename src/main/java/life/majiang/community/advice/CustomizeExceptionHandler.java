package life.majiang.community.advice;

import life.majiang.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CustomizeExceptionHandler {
    // 所有在后端代码中被显式throw出的异常都可以用这个方法捕获处理
    // 非后端throw出的异常（如前端直接输一个无效的地址http://localhost:8887/111），则需要新加一个CustomizeErrorController进行捕获处理
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable e, Model model) {
        HttpStatus status = getStatus(request);

        if(e instanceof CustomizeException){
            model.addAttribute("message",e.getMessage());
        }
        else{
            model.addAttribute("message","服务器冒烟了");
        }
        return new ModelAndView("error");
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
