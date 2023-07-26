package com.zhang.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author shkstart
 * @create 2023-07-19 20:56
 */
/*
配置分页插件
 */
    @Configuration
public class MyBatisPlusConfig {

       @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
           //创建MP拦截器
           MybatisPlusInterceptor mpi = new MybatisPlusInterceptor();
           // 添加分页拦截器
           mpi.addInnerInterceptor(new PaginationInnerInterceptor());
           return mpi;
       }
}

