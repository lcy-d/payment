package com.yu.payment.exception;

import com.yu.payment.domain.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Controller
public class XdExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public JsonData Handler(Exception e) {
        if (e instanceof XdException) {
            XdException xdException = (XdException) e;
            return JsonData.buildError(xdException.getMsg(), xdException.getCode());
        } else if (e instanceof NullPointerException) {
           return JsonData.buildError("空指针异常");
        } else {
            return JsonData.buildError("全局异常，未知错误");
        }
    }
}
