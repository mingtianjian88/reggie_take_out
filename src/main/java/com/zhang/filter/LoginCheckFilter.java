package com.zhang.filter;
import com.alibaba.fastjson.JSON;
import com.zhang.common.BaseContext;
import com.zhang.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * 检查用户是否已经完成登录
 * */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    // 路径匹配器
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1、获取本次请求的URL
        // 定义不需要处理的请求路径
        String requestURL = request.getRequestURI();
        log.info("拦截到的请求地址："+requestURL);
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };
        // 2、判断本次请求是否需要处理
        boolean check = check(urls, requestURL);

        // 3、如果不需要处理，则直接放行
        if(check){
            filterChain.doFilter(request, response);
            return;
        }

        // 4、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employeeId") != null){
            Long employeeId = (Long) request.getSession().getAttribute("employeeId");
            BaseContext.setCurrentId(employeeId);
            filterChain.doFilter(request, response);
            return;
        }
            // 4、判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("user") != null){
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request, response);
            return;
        }


//         filterChain.doFilter(request,response);
//         5、如果未登录则返回未登录结果，通过输出流方式向客户端页面响应数据
        else {
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return;
        }
    }

    /**
     * 路径匹配
     * @param urls
     * @param requestURL
     * @return
     */
    public boolean check(String[] urls, String requestURL){
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURL);
            if(match){
                return true;
            }
        }
        return false;
    }
}
