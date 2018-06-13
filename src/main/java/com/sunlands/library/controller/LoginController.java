package com.sunlands.library.controller;


import com.sunlands.library.domain.User;
import com.sunlands.library.service.UserService;
import com.sunlands.library.util.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author hulin
 */
@Controller
public class LoginController {

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @RequestMapping({"/","/index"})
    public String index(HttpServletRequest request){
        //这里的cookie被加密了，没法使用
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie:cookies){
            if ("rememberMe".equals(cookie.getName())){
                System.out.println(cookie.getValue()+"!!!");
            }
        }
        return"/index";
    }

    @RequestMapping("/manageUI")
    public String index3(Model model){
        //主体
        Subject subject = SecurityUtils.getSubject();

        User user = (User) subject.getPrincipal();

        model.addAttribute("loginUser",user);
        return"/manageUI";
    }

    @RequestMapping(value = "/ajaxLogin")
    @ResponseBody
    public Result index(User user,String rememberMe) {
        String msg = null;
        if (user.getUserName() != null && user.getPassword() != null) {
            UsernamePasswordToken token = new UsernamePasswordToken(user.getUserName(), user.getPassword(), "login");
            if("true".equals(rememberMe)){
                token.setRememberMe(true);
            }

            Subject currentUser = SecurityUtils.getSubject();

            logger.info("对用户[" + user.getUserName() + "]进行登录验证..验证开始");
            try {
                currentUser.login( token );
                //验证是否登录成功
                if (currentUser.isAuthenticated()) {
                    logger.info("用户[" + user.getUserName() + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
                    return Result.succeed("/manageUI");
                } else {
                    token.clear();
                    System.out.println("用户[" + user.getUserName() + "]登录认证失败,重新登陆");
                    return Result.fail(403,"登录认证失败,重新登陆");
                }
            } catch ( UnknownAccountException uae ) {
                logger.info("对用户[" + user.getUserName() + "]进行登录验证..验证失败-username wasn't in the system");
                msg="用户名不存在";
            } catch ( IncorrectCredentialsException ice ) {
                logger.info("对用户[" + user.getUserName() + "]进行登录验证..验证失败-password didn't match");
                msg="密码不正确";
            } catch ( LockedAccountException lae ) {
                logger.info("对用户[" + user.getUserName() + "]进行登录验证..验证失败-account is locked in the system");
                msg="账户被锁定";
            }
        }
        return Result.fail(403,msg);
    }

    @RequestMapping("/logout")
    @ResponseBody
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return Result.succeed("/index");
    }

    @RequestMapping(value="/login",method=RequestMethod.POST)
    public String login(HttpServletRequest request, Map<String, Object> map) throws Exception {
        System.out.println("HomeController.login()");
        // 登录失败从request中获取shiro处理的异常信息。
        // shiroLoginFailure:就是shiro异常类的全类名.
        String exception = (String) request.getAttribute("shiroLoginFailure");

        System.out.println("exception=" + exception);
        String msg = "";
        if (exception != null) {
            if (UnknownAccountException.class.getName().equals(exception)) {
                System.out.println("UnknownAccountException -- > 账号不存在：");
                msg = "UnknownAccountException -- > 账号不存在：";
            } else if (IncorrectCredentialsException.class.getName().equals(exception)) {
                System.out.println("IncorrectCredentialsException -- > 密码不正确：");
                msg = "IncorrectCredentialsException -- > 密码不正确：";
            } else if ("kaptchaValidateFailed".equals(exception)) {
                System.out.println("kaptchaValidateFailed -- > 验证码错误");
                msg = "kaptchaValidateFailed -- > 验证码错误";
            } else {
                msg = "else >> "+exception;
                System.out.println("else -- >" + exception);
            }
        }
        map.put("msg", msg);
        // 此方法不处理登录成功,由shiro进行处理.
        return "/login";
    }
}
