package EndDeno.excepter;


/**
 *  全局异常处理
**/



import EndDeno.domain.Result;
import EndDeno.exception.RemoveCategotyException;
import EndDeno.exception.RemoveDishException;
import EndDeno.exception.RemoveSetmealException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
//结果会封装为Json数据
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

  /** 处理多次注册同一用户名的异常
    *
*/
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHanlder(SQLIntegrityConstraintViolationException exception){
        //获取异常信息
        //log.error(exception.getMessage());
        return Result.error("名称已被注册");

    }

    /*
    处理删除菜品或套餐分类时由于id关联产生的异常
    进行异常捕获*/
    @ExceptionHandler(RemoveCategotyException.class)
    public Result<String> RemoveCategotyExceptionHanlder(RemoveCategotyException exception){
        //获取异常信息
        //log.error(exception.getMessage());
        return Result.error(exception.getMessage());

    }

    /*
    * 处理删除套餐时删除了包含在售状态的套餐而抛出的异常
    * */
    @ExceptionHandler(RemoveDishException.class)
    public Result<String> RemoveDishExceptionHanlder(RemoveDishException removeDishException){
        return Result.error(removeDishException.getMessage());
    }

    /*
     * 处理删除套餐时删除了包含在售状态的套餐而抛出的异常
     * */
    @ExceptionHandler(RemoveSetmealException.class)
    public Result<String> RemoveSetmealExceptionHanlder(RemoveSetmealException setmealException){
        return Result.error(setmealException.getMessage());
    }
}
