package com.zhang.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author shkstart
 * @create 2023-07-19 17:05
 */

/*
异常处理器
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class ProjectExceptionAdvice {

//    当输入已存在的用户名时，异常为 SQLIntegrityConstraintViolationException
    //异常信息为 Duplicate entry 'zhangsan' for key 'idx_username'
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> doException(SQLIntegrityConstraintViolationException e){
        //先判断信息中是否有 Duplicate entry 关键字
        if (e.getMessage().contains("Duplicate entry")) {
            //如果有 举例：Duplicate entry 'zhangsan' for key 'idx_username'
            //用空格将这个字符串给分割开，对分割后得到的字符串数组的第三位字符，存入失败信息
            String message = e.getMessage();
            String[] s = message.split(" ");
            String errMes = s[2];
            return R.error("您输入的 "+errMes+" 已经存在！");
        }
        return R.error("未知的错误！");
    }

    @ExceptionHandler(MyException.class)
    public R<String> doMyException(MyException e){
        return R.error(e.getMessage());
    }


}
