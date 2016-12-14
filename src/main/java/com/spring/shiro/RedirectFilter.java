package com.spring.shiro;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * session过期重定向拦截
 */
public class RedirectFilter extends UserFilter {

    public final static String X_R = "X-Requested-With";
    public final static String X_R_VALUE = "XMLHttpRequest";

    /**
     * 加入ajax查询参数，以便跳转至超时登录页面。
     * ajax访问超时不能返回登录界面，所有有此方法来协调
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    protected void redirectToLogin(ServletRequest request,
                                   ServletResponse response) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String xrv = httpServletRequest.getHeader(X_R);

		/*
         * 如果是ajax访问
		 */
        if (xrv != null && xrv.equalsIgnoreCase(X_R_VALUE)) {
            //重定向到/ajax,不然会自动重定向到登录页
            ((HttpServletResponse) response).sendRedirect("/ajax");
        } else {
            super.redirectToLogin(request, response);
        }
    }

    @Override
    protected void saveRequest(ServletRequest request) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String xrv = httpServletRequest.getHeader(X_R);
        /*
         * 如果是ajax访问
		 */
        if (xrv != null && xrv.equalsIgnoreCase(X_R_VALUE)) {
            //WebUtils.saveRequest(request);
        } else {
            WebUtils.saveRequest(request);
        }
    }
}
