package com.zhang;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author shkstart
 * @create 2023-07-17 22:48
 */
@Slf4j//日志
@SpringBootApplication
@ServletComponentScan//扫描过滤器
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
        log.info("项目资源启动了...");
    }
}
