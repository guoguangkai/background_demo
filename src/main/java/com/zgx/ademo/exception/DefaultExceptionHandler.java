package com.zgx.ademo.exception;

import com.zgx.ademo.result.Result;
import com.zgx.ademo.result.ResultFactory;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理，捕获所有Controller中抛出的异常。
 */
/*@ControllerAdvice，是Spring3.2提供的新注解,它是一个Controller增强器,可对controller中被 @RequestMapping注解的方法加一些逻辑处理。
统一异常处理,需要配合@ExceptionHandler使用。当将异常抛到controller时,可以对异常进行统一处理,规定返回的json格式或是跳转到一个错误页面
异常集中处理，更好的使业务逻辑与异常处理剥离开*/
@ControllerAdvice
public class DefaultExceptionHandler {
    //@ExceptionHandler：统一处理某一类异常，从而能够减少代码重复率和复杂度。（Exception.class)处理所有类型的异常
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handleAuthorizationException(UnauthorizedException e) {
        String message = "权限认证失败";
        return ResultFactory.buildFailResult(message);
    }
}

