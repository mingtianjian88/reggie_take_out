package com.zhang.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhang.pojo.Category;
import com.zhang.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author shkstart
 * @create 2023-07-20 21:36
 */
@SpringBootTest
public class TestUpLoad {

    @Autowired
    private CategoryService categoryService;
    @Test
    public void test1(){
        System.out.println(UUID.randomUUID().toString());
    }


    @Test
    public void test2(){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Category::getType,1);
          List<Category> list = categoryService.list(queryWrapper);

        for (Category category  : list) {
            System.out.println(category);
        }

    }
    @Test
    public void test03(){
        String pattern = "^[1-9]\\d{4,10}@qq.com";
    }

}
