package com.zhang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.pojo.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shkstart
 * @create 2023-07-20 17:49
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
