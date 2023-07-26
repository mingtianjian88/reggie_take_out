package com.zhang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.pojo.Category;

/**
 * @author shkstart
 * @create 2023-07-20 15:46
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long ids);
}
