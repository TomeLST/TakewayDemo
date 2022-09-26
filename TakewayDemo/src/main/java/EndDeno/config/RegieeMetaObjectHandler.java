package EndDeno.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/*
* 自定义提供的元数据处理器
* 对公共字段的管理
*
*/

@Component
@Slf4j
public class RegieeMetaObjectHandler implements MetaObjectHandler {
  /*  * 插入操作自动填充
    *
*/
    @Override
    public void insertFill(MetaObject metaObject) {
        /*log.info("公共字段insert自动填充");
        log.info(metaObject.toString());
        Long id =Thread.currentThread().getId();
        log.info("当前线程的id:{}",id);*/

        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", ThreadLocalSet.getCurreentId());
        metaObject.setValue("updateUser", ThreadLocalSet.getCurreentId());

    }

 /*   * 更新操作自动填充
    *
*/
    @Override
    public void updateFill(MetaObject metaObject) {
        /*log.info("公共字段update自动填充");
        log.info(metaObject.toString());
        Long id =Thread.currentThread().getId();
        log.info("当前线程的id:{}",id);*/
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", ThreadLocalSet.getCurreentId());
    }
}
