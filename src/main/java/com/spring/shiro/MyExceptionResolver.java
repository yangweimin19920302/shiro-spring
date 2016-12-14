package com.spring.shiro;

import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类名称：MyExceptionResolver.java
 * 类描述：自定义捕获异常类(此处用来抓取没有操作权限是抛出的异常)
 *
 * @author lsq
 *         作者单位：
 *         联系方式：QQ237442461
 * @version 1.0
 */
public class MyExceptionResolver implements HandlerExceptionResolver {

    public final static String X_R = "X-Requested-With";
    public final static String X_R_VALUE = "XMLHttpRequest";

    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response, Object handler, Exception ex) {
        //UnauthorizedException是shiro无权操作异常
        if (ex instanceof UnauthorizedException) {
            String xrv = request.getHeader(X_R);
            //如果是ajax访问
            if (xrv != null && xrv.equalsIgnoreCase(X_R_VALUE)) {
                ModelAndView mv = new ModelAndView("/ajax_403");
                return mv;
            } else {
                ModelAndView mv = new ModelAndView("/403");
                return mv;
            }
        } else {
            ModelAndView mv = new ModelAndView("/404");
            return mv;
        }
    }
}