package com.kabunx.erp.advice;

import com.kabunx.erp.domain.JsonResponse;
import com.kabunx.erp.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 统一的全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = BizException.class)
    public JsonResponse<Object> handle(BizException e) {
        log.error(e.getMessage());
        if (e.getErrorCode() != null) {
            return JsonResponse.failed(e.getErrorCode());
        }
        return JsonResponse.failed(e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public JsonResponse<Object> handleValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return JsonResponse.validateFailed(collectFieldErrors(e.getBindingResult()));
    }

    @ExceptionHandler(value = BindException.class)
    public JsonResponse<Object> handleValidException(BindException e) {
        log.error(e.getMessage());
        return JsonResponse.validateFailed(collectFieldErrors(e.getBindingResult()));
    }

    /**
     * 错误信息收集
     *
     * @param bindingResult 绑定结果
     * @return 错误信息
     */
    private HashMap<String, ArrayList<String>> collectFieldErrors(BindingResult bindingResult) {
        HashMap<String, ArrayList<String>> errors = new HashMap<>();
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                ArrayList<String> fErrors = errors.computeIfAbsent(fieldError.getField(), k -> new ArrayList<>());
                fErrors.add(fieldError.getDefaultMessage());
            }
        }
        return errors;
    }
}
