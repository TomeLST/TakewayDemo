package EndDeno.config;
/*
* 用于保存和获取在公共字段填充中同一线程内的用户id
* 基于threadLocal*/
public class ThreadLocalSet {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();


    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurreentId(){
        return threadLocal.get();
    }
}
