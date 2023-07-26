package com.zhang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.dto.DishDto;
import com.zhang.mapper.DishDtoMapper;
import com.zhang.service.DishDtoService;
import org.springframework.stereotype.Service;

/**
 * @author shkstart
 * @create 2023-07-21 20:00
 */
@Service
public class DishDtoServiceImpl extends ServiceImpl<DishDtoMapper, DishDto> implements DishDtoService {
}
