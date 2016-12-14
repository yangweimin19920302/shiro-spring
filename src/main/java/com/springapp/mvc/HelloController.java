package com.springapp.mvc;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.ShardedJedisPool;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(produces = "application/json;charset=UTF-8")
public class HelloController {

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String printWelcome(ModelMap model) {
        model.addAttribute("message", "Hello world!");
        return "hello";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(ModelMap model) {
        model.addAttribute("message", "这是一张测试shiro能否记住上一次页面的页面，如果在改页面session过期了，那么重新登录时，如果能跳转到此页面，说明成功");
        return "test";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(ModelMap model) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "redirect:/welcome";
        } else {
            return "login";
        }
    }

    @ResponseBody
    @RequestMapping(value = "/initlogin", method = RequestMethod.GET)
    public Object initlogin(String name, String password, HttpServletRequest request) throws Exception{
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(name, password);
        SavedRequest savedRequest = WebUtils.getSavedRequest(request);
        try {
            subject.login(token);
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            throw new CustomException(301,"账号不存在");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            throw new CustomException(302,"密码不正确");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        }
        if (savedRequest == null || savedRequest.getRequestUrl() == null) {
            return null;
        } else {
            return savedRequest.getRequestUrl();
        }
        //return null;
    }

    @ResponseBody
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @RequiresPermissions("user:edit")
    public Object get() throws Exception {
        //Thread.sleep(5000);
        return "get";
    }

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @RequiresPermissions("user:add")
    public Object add() throws Exception {
        return "add";
    }

    @RequestMapping(value = "/loginout", method = RequestMethod.GET)
    public String loginout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/welcome";
    }

    /**
     * ajax登录超时
     *
     * @return String
     */
    @RequestMapping("/ajax")
    @ResponseBody
    public Object outTime() throws Exception {
        throw new CustomException(300, "登录超时");
    }

}