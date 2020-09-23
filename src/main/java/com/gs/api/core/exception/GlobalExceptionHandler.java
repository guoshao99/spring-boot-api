package com.gs.api.core.exception;

import com.gs.api.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description TODO 全局异常捕捉处理
 * @Author GuoShao
 * @Date 2020/9/23 19:15
 * @Version 1.0
 **/

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Description : 全局异常捕捉处理
     */
    @ExceptionHandler(RRException.class)
    public Result apiExceptionHandler(HttpServletRequest req, RRException ex) {
        log.error("ApiException 异常抛出请求地址：{}", req.getRequestURI());
        Result r = new Result();
        r.setMsg(ex.getMessage());
        r.setCode(500);
        r.setData("not data");
        return r;
    }

}
