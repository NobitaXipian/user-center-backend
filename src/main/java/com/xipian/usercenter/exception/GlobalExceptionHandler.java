package com.xipian.usercenter.exception;

import com.xipian.usercenter.common.BaseResponse;
import com.xipian.usercenter.common.ErrorCode;
import com.xipian.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.xipian.usercenter.common.ErrorCode.SYSTEM_ERROR;

/**
 * 全局异常处理
 *
 * @author xipian
 * @date 2023/9/17
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessExceptionHandler(BusinessException e){
        log.error("businessException: ",e.getMessage(),e);
        return ResultUtils.error(e.getCode(),e.getMessage(),e.getDescription());

    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeExceptionHandler(RuntimeException e){
        log.error("runtimeException: ",e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR,e.getMessage(),"");
    }
}
