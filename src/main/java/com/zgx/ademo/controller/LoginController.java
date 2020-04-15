package com.zgx.ademo.controller;

import com.zgx.ademo.entity.User;
import com.zgx.ademo.result.Result;
import com.zgx.ademo.result.ResultFactory;
import com.zgx.ademo.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    // @RequestBody接收请求体中的数据。前端必须用POST方式进行提交。
    // @RequestParam接收的是key-value里面的参数，如果参数前写了@RequestParam(xxx)，那么前端必须有对应的xxx名字才行(不管其是否有值，当然可以通过设置该注解的required属性来调节是否必须传)
    //如果没有xxx名的话，那么请求会出错，报400。如果参数前不写@RequestParam(xxx)的话，那么就前端可以有可以没有对应的xxx名字才行，如果有xxx名的话，那么就会自动匹配；没有的话，请求也能正确发送。
    // 出于安全原因，浏览器限制从脚本内发起的跨源HTTP请求，禁止Ajax调用驻留在当前原点之外的资源。SpringMVC4.2+支持@CrossOrigin跨域。
    // 1：controller方法(类)的CORS配置，您可以向@RequestMapping注解处理程序方法添加一个@CrossOrigin注解，以便启用CORS（默认情况下，@CrossOrigin允许在@RequestMapping注解中指定的所有源和HTTP方法）
    // 2:其中@CrossOrigin中的2个参数：origins:允许可访问的域列表 maxAge：准备响应前的缓存持续的最大时间（以秒为单位）。
    // 3:除了细粒度、基于注释的配置之外，您还可能需要定义一些全局CORS配置。这类似于使用筛选器，但可以声明为Spring MVC并结合细粒度@CrossOrigin配置。
    //   默认情况下，所有origins and GET, HEAD and POST methods是允许的。【编写配置类，继承WebMvcConfigurerAdapter，重写addCorsMappings方法[registry.addMapping("/**")]】
    @CrossOrigin
    @PostMapping(value = "api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser, HttpSession session) {
        String username = requestUser.getUsername();
        // 对 html 标签进行转义，防止 XSS 攻击。【"<div>hello world</div><p>&nbsp;</p>"  转义后为 &lt;div&gt;hello world&lt;/div&gt;&lt;p&gt;&amp;nbsp;&lt;/p&gt; 】
        username = HtmlUtils.htmlEscape(username);

        /*if (!Objects.equals("admin", username) || !Objects.equals("123456", requestUser.getPassword())) {
            String message = "账号密码错误";
            System.out.println("test");
            return new Result(400);
        } else {
            return new Result(200);
        }*/
        //为了保存登录状态，我们可以把用户信息存在 Session 对象中（当用户在应用程序的 Web 页之间跳转时，存储在 Session 对象中的变量不会丢失），这样在访问别的页面时，可以通过判断是否存在用户变量来判断用户是否登录。
        User user = userService.get(username, requestUser.getPassword());
        if (null == user) {
            return new Result(400);
        } else {
            session.setAttribute("user", user);
            return new Result(200);
        }
    }

    @PostMapping("api/register")
    @ResponseBody
    public Result register(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        username = HtmlUtils.htmlEscape(username);
        user.setUsername(username);

        boolean exist = userService.isExist(username);
        if (exist) {
            String message = "用户名已被使用";
            return ResultFactory.buildFailResult(message);
        }

        // 生成盐,默认长度 16 位,干扰数据 盐 防破解    先生成了随机的 byte 数组，又转换成了字符串类型的 base64 编码并返回。
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        // 设置 hash 算法迭代次数
        int times = 2;
        // 得到 hash 后的密码   散列算法类型为MD5
        String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
        // 存储用户信息，包括 salt 与 hash 后的密码
        user.setSalt(salt);
        user.setPassword(encodedPassword);
        userService.register(user);

        return ResultFactory.buildSuccessResult(user);
    }

    //使用shiro实现的登录
    @PostMapping(value = "/api/login")
    @ResponseBody
    public Result login(@RequestBody User requestUser) {
        String username = requestUser.getUsername();
        Subject subject = SecurityUtils.getSubject();
//        subject.getSession().setTimeout(10000);
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, requestUser.getPassword());
        usernamePasswordToken.setRememberMe(true);
        try {
            //只需要调用一句 subject.login(usernamePasswordToken) 就可以执行验证
            //Shiro 通过 Realm 里我们重写的 doGetAuthenticationInfo 方法获取到了验证信息，再根据我们在配置类里定义的 CredentialsMatcher（HashedCredentialsMatcher）
            subject.login(usernamePasswordToken);
            return ResultFactory.buildSuccessResult(usernamePasswordToken);
        } catch (AuthenticationException e) {
            String message = "账号密码错误";
            return ResultFactory.buildFailResult(message);
        }
    }

    //登出
   /* 核心就是 subject.logout() 默认 Subject 接口是由 DelegatingSubject 类实现。其 logout 方法就是：
    清除 session、principals，并把 authenticated 设置为 false*/
    /*之前我们在后端配置了拦截器，由于登出功能不需要被拦截，所以我们还需要修改配置类 MyWebConfigurer 的 addInterceptors() 方法，添加一条路径*/
    @ResponseBody
    @GetMapping("api/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        String message = "成功登出";
        return ResultFactory.buildSuccessResult(message);
    }
}
