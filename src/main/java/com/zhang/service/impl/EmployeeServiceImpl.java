package com.zhang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.mapper.EmployeeMapper;
import com.zhang.pojo.Employee;
import com.zhang.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author shkstart
 * @create 2023-07-18 11:49
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
