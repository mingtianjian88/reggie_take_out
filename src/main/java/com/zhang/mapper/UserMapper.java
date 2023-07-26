package com.zhang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhang.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author shkstart
 * @create 2023-07-23 19:43
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
