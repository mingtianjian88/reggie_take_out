package com.zhang.common;

/**
 * @author shkstart
 * @create 2023-07-20 11:21
 */

/**
 *  用来封装ThreadLocal对象，保存和设置用户Id
 */
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
     public static Long getCurrentId(){
        return threadLocal.get();
    }

}
