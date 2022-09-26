package EndDeno.filter;

import EndDeno.config.ThreadLocalSet;
import EndDeno.domain.Result;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* 检查用户是否完成登录
* */
@WebFilter(filterName = "loginChnekFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器 支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest =(HttpServletRequest)request;
        HttpServletResponse servletResponse =(HttpServletResponse) response;
        //1.获取本次请求的uri
        String requesturi = servletRequest.getRequestURI();
        //2.判断用户的登录状态，处理请求
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**"

        };
        //3.请求若不需要处理则直接放行
        boolean check = check(urls,requesturi);
        if(check){
            chain.doFilter(servletRequest,servletResponse);
            return;
        }
        //4.需要处理则需要判断用户是否登录 如果登录则放行
       if (servletRequest.getSession().getAttribute("employeeId")!=null){
           ///Long Id =Thread.currentThread().getId();
          // log.info("当前过滤器线程的id:{}",Id);
           Long id = (Long) servletRequest.getSession().getAttribute("employeeId");
           ThreadLocalSet.setCurrentId(id);
          // log.info("当前过滤器获取的用户的id:{}",Id);
           chain.doFilter(servletRequest,servletResponse);
       }

       //5.如果未登录则返回给前端页面数据进行处理 request.js

     /*  servletResponse.getWriter().write(JSON.toJSONString(Result.error("NOTLOGIN")));

       return;*/



    }
    /*
     * 路径匹配 判断本次请求是否需要放行
     * */

    public boolean check(String[] urls,String Requesturi){
        for (String url :urls ) {
            boolean match = PATH_MATCHER.match(url,Requesturi);
            if(match){
                return true;
            }

        }
        return false;
    }
}
