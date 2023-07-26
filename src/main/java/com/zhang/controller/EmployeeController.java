package com.zhang.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhang.common.R;
import com.zhang.pojo.Employee;
import com.zhang.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;


/**
 * @author shkstart
 * @create 2023-07-18 12:38
 */
@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;


    /**
     * 员工登陆
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
//        1、将页面提交的密码password进行md5加密处理
        //        通过一个工具类DigestUtils的md5DigestAsHex对密码进行加密
        String password = employee.getPassword();
         password = DigestUtils.md5DigestAsHex(password.getBytes());


//  2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<Employee>();
        LambdaQueryWrapper<Employee> queryWrapper = lqw.eq(Employee::getUsername, employee.getUsername());
        //因为在数据库中对username进行了一个唯一索引，所以用getOne
        Employee emp = employeeService.getOne(queryWrapper);
//    3、如果没有查询到则返回登录失败结果
        if (emp == null){
            return R.error("登陆失败！");
        }
//        4密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)){
            return R.error("登陆失败！");
        }
//    5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0){
            return R.error("该员工信息已被禁用!");
        }
//    6、登录成功，将员工id存入Session并返回登录成功结果
        HttpSession session = request.getSession();
        session.setAttribute("employeeId",emp.getId());
        return  R.success(emp);
    }



    /**
     * 管理员退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
//        1. 清理Session中的用户ID
        request.removeAttribute("employeeId");
//        2. 返回结果实体类
        return R.success("退出成功!");
    }



    /**
     * 分页处理员工信息
     * @param pageSize
     * @param page
     * @param name
     * @return
     */
    @GetMapping("/page")
    //根据前端的代码，这个的分页处理返回的对象应该是一个Page对象，将查询到的所有数据封装到Page对象中，然后返回
    //这里的参数name是因为，页面有个查询功能，可能会传过来一个name
   public  R<Page>  page(@RequestParam Integer pageSize,@RequestParam Integer page,String name){
//        System.out.println(page);
//        System.out.println(pageSize);
        //创建Page对象
        Page<Employee> pageObj = new Page<>(page,pageSize);
        //创建sql条件查询语句
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        //根据名字查询，建议使用like
        LambdaQueryWrapper<Employee> queryWrapper = lqw.like(name != null, Employee::getName, name);
        //并且按照更新时间来排序
          queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行分页方法
        employeeService.page(pageObj,lqw);
        return R.success(pageObj);
    }



    /**
     * 添加员工信息
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
      //因为前端已经校验好了，所以这里的employee不可能为空

        log.info(employee.toString());
        //因为前端注册员工信息时，没有密码输入,这里将密码初始值设置为123456并且加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//
//        //将登陆的用户的ID存入 create_User
//        employee.setCreateUser((Long) request.getSession().getAttribute("employeeId"));
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employeeId"));

        employeeService.save(employee);
       return R.success("保存成功！");
    }



    /**
     * 更新操作
     * @param request
     * @param employee
     * @return
     */
    @PutMapping
        public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        //这里还需要更新一下update_time 以及  update_user
        Long id = (Long)request.getSession().getAttribute("employeeId");
        employee.setUpdateUser(id);
        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return R.success("修改成功！");
    }




    /**
     * 更新雇员，这里的方法只是根据id将对应的雇员查找出来，然后回显到页面上，最后的更新还是走上面的更新操作
     * @param request
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(HttpServletRequest request,@PathVariable Integer id){
        Employee emp = employeeService.getById(id);
        if (emp != null){
        return R.success(emp);
        }
        return R.error("没有查询到该雇员！");
    }





}
