package EndDeno.exception;

/*
* 自定义异常类
* 处理删除菜品或套餐分类时由于id关联产生的异常
* */

public class RemoveCategotyException extends RuntimeException{
    public RemoveCategotyException(String message){
        super(message);
    }
}
