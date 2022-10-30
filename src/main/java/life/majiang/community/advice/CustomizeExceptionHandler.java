package life.majiang.community.advice;

import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
public class CustomizeExceptionHandler {
    // 所有在后端代码中被显式throw出的异常都可以用这个方法捕获处理
    // 这里集成了两个功能：
    // 若抛出的是"恶意"异常，判定标准：contentType为text/html，则直接跳转到error白页处理
    // 若抛出的是"友好"异常，判定标准：contentType为application/json，则返回给前端一个json进行响应，让前端对这个错误进行响应的展示和处理，这样对发出友好异常的用户也较为友好
    // 非后端throw出的异常（如前端直接输一个无效的地址http://localhost:8887/111），则需要新加一个CustomizeErrorController进行捕获处理
    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, Throwable e, Model model, HttpServletResponse response) {
//        HttpStatus status = getStatus(request);
        String contentType=request.getContentType();
        if(contentType.equals("application/json")){
            // 返回json
            ResultDTO resultDTO;
            if (e instanceof CustomizeException) {
                // 手动编写throw出的CustomizeException异常
                // 如throw new CustomizeException(CustomizeErrorCode.QUSETION_NOT_FOUND);
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            } else {
                // 手动编写throw出的非CustomizeException异常，或者非手动编写throw出的异常（如代码逻辑错误throw出的异常，一些调用函数内部自带的异常等）
                resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ioe) {
            }
            return null;
        }
        else{
            // 跳转回error空白页
            if(e instanceof CustomizeException){
                model.addAttribute("message",e.getMessage());
            }
            else{
                model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }

//    private HttpStatus getStatus(HttpServletRequest request) {
//        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
//        if (statusCode == null) {
//            return HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        return HttpStatus.valueOf(statusCode);
//    }
}
