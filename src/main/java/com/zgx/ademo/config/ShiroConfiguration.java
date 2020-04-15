package com.zgx.ademo.config;

import com.zgx.ademo.filter.URLPathMatchingFilter;
import com.zgx.ademo.shiro.MyRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

//Shiro 的配置类,这个配置类里决定了一些关键的选择。
@Configuration
public class ShiroConfiguration {
    //在Spring和Shiro进行整合时，配置某些bean的时候都需要配置LifecycleBeanPostProcessor。管理shiro一些bean的生命周期。
    //其实配置shiro，不需要配置生命周期处理器，shiro框架整合spring时，这个事情不会交给使用者去做，自己会注入处理器。
    // 查看shiro-spring jar下面的config包里的ShiroBeanConfiguration，已经注入了。在此注入纯属多此一举
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(getMyRealm());
        return securityManager;
    }

    @Bean
    public MyRealm getMyRealm() {
        MyRealm myRealm = new MyRealm();
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myRealm;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
    //为了启用 rememberMe
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        cookieRememberMeManager.setCipherKey("EVANNIGHTLY_WAOU".getBytes());
        return cookieRememberMeManager;
    }
    //为了启用 rememberMe 配置新状态的cookies的存活时间 Max-Age=259200，单位是秒，259200 即 30 天
    @Bean
    public SimpleCookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }
    //增加获取过滤器的方法，注意这里不能使用 @Bean
    public URLPathMatchingFilter getURLPathMatchingFilter() {
        return new URLPathMatchingFilter();
    }
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl("/nowhere");

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        Map<String, Filter> customizedFilter = new HashMap<>();  // 自定义过滤器设置 1

        customizedFilter.put("url", getURLPathMatchingFilter()); // 自定义过滤器设置 2，命名，需在设置过滤路径前
     /*   我们平时使用就是anno，任何人都可以访问；authc：必须是登录之后才能进行访问，不包括remember me；user：登录用户才可以访问，包含remember me；
        perms：指定过滤规则，这个一般是扩展使用，不会使用原生的；其中filterChainDefinitions 就是指定过滤规则的，一般公共配置使用配置文件，
        例如jss css img这些资源文件是不拦截的，相关业务的url配置到数据库，有过滤器查询数据库进行权限判断。*/
        filterChainDefinitionMap.put("/api/menu", "authc");
        filterChainDefinitionMap.put("/api/admin/**", "authc");
        filterChainDefinitionMap.put("/api/admin/**", "url");  // 自定义过滤器设置 3，设置过滤路径
    /*  拦截器的优先级：从上到下，从左到右，如果有匹配的拦截器就会阻断并返回，例如：访问js/a.js,第一个拦截器anon符合，就返回true了，不在往下进行匹配了，
        注意最后一个拦截最后一句是/**=user,frameperms 意思就是除了上面的那些,其他的所有都要经过 ，user和frameperms.如果没有登陆 user就会阻断,
         不会执行到frameperms.frameperms 就是我们自定义实现的过滤器,从数据库中查询用户的权限,判断当前用户是否有权限访问拦截的url.*/
        shiroFilterFactoryBean.setFilters(customizedFilter); // 自定义过滤器设置 4，启用
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
        /*OK，这时候你可以测试一下，使用 editor 账号（密码 123）登录，由于获取不到对应权限，请求自动转发到 \login，不过由于我们是前后端分离的，所以\login
       这个接口并不存在，可以通过 shiroFilterFactoryBean.setLoginUrl() 方法手动设置跳转路径，不过没有必要，我们在前端配置的规则会引发路由跳转到登录页面。
        上面的 filterChainDefinitionMap.put("/api/authentication", "authc"); 是我们的防前端鸡贼登录规则，实际上由于访问后台页面先要查询菜单，这个规则是多余的，
        姑且先留着它吧。
        这个 authc 即 autentication，是 shiro 自带的过滤器。除了它以外，常用的还有 anon（可匿名访问）、roles（需要角色）、perms（需要权限）等。
        讲到这里你可能有疑问，为啥我们不直接用 perms 呢？
        其实使用 perms 才是 Shiro 的祖传解决方案，但是为了配合它的实现，我们需要在配置文件中添加规则如filterChainDefinitionMap.put("/api/authentication", "perms[/api/admin/user]")，
        或者在接口处编写注解如 @RequirePermission("/api/admin/user") ，这样如果我们想要删除或新增权限，除了修改数据库外还需要重新编写源码，这就比较蓝瘦了。
        此外，自带过滤器会拦截 options 请求，所以在前后端分离的项目里使用自定义过滤器反而简便一些。。。
        不过，其实对很多项目来说不太需要动态增删权限，只需要对现有权限进行分配就够了，所以在不分离的情况下使用自带过滤器当然更好。*/
    }
}


