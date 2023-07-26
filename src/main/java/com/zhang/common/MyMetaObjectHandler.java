package com.zhang.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

/**
 * @author shkstart
 * @create 2023-07-20 10:46
 */
@Component
@Slf4j
//元对象处理器,填入公共字段
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 处理插入时，填充
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //使用threadLocal来动态获取createUser和updateUser 的值
        //因为同一个线程Thread共享同一个ThreadLocal域
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());

        metaObject.setValue("createUser",new Long(1));
        metaObject.setValue("updateUser",new Long(1));

    }

    /**
     * 处理更新时，填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser",new Long(1));
    }
}
