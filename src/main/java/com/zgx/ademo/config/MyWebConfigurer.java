package com.zgx.ademo.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*标注这个类是一个配置类，就像xml配置文件，而现在是用java配置文件.
并会将当前类内声明的一个或多个以@Bean注解标记的方法的实例纳入到spring容器中，并且实例名就是方法名。
只是@Configuration注解的派生注解，跟@Configuration注解的功能一致，
@SpringBootConfiguration是springboot的注解，而@Configuration是spring的注解*/
@SpringBootConfiguration
public class MyWebConfigurer implements WebMvcConfigurer {
/*WebMvcConfigurer配置类其实是Spring内部的一种配置方式，采用JavaBean的形式来代替传统的xml配置文件形式进行针对框架个性化定制，可以自定义一些addInterceptors：拦截器,addViewControllers：
页面跳转,addResourceHandlers：静态资源,configureDefaultServletHandling：默认静态资源处理器,configureViewResolvers：视图解析器,
    configureContentNegotiation：配置内容裁决的一些参数,addCorsMappings：跨域,configureMessageConverters：信息转换器,*/
/*Spring的@Bean注解用于告诉方法，产生一个Bean对象，然后这个Bean对象交给Spring管理。(和xml配置中的bean标签的作用是一样的)*/
/**    之前我们在做登录拦截的时候使用了拦截器，即 Interceptor。由于 Shiro 的权限机制要靠它自身提供的过滤器实现，所以我们现在弃用之前的拦截器。
    首先在 MyWebConfigurer 中删除拦截器配置代码：**/
   /** @Bean
    public LoginInterceptor_bak getLoginIntercepter() {
        return new LoginInterceptor_bak();
    }**/
    //注册拦截器
   /** @Override
    public void addInterceptors(InterceptorRegistry registry){**/
        //addInterceptor：需要一个实现HandlerInterceptor接口的拦截器实例。addPathPatterns：用于设置拦截器的过滤路径规则；
        // addPathPatterns("/**")对所有请求都拦截；excludePathPatterns：用于设置不需要拦截的过滤规则
       /** registry.addInterceptor(getLoginIntercepter())
                .addPathPatterns("/**")
                .excludePathPatterns("/index.html")
                .excludePathPatterns("/api/login")
                .excludePathPatterns("/api/logout")
                .excludePathPatterns("/api/books");**/
        /*    静态资源； *.css,*.js
        SpringBoot已经做好了静态资源映射
        registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**")
        .excludePathPatterns("/index.html","/","/user/login","/static/**","/webjars/**");*/
   /** }**/
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/file/**").addResourceLocations("file:" + "d:/workspace/img/");
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //addMapping：配置可以被跨域的路径，可以任意配置，可以具体到直接请求路径。
        registry.addMapping("/**")
                //这里注意，在 allowCredentials(true) ，即允许跨域使用 cookie 的情况下，allowedOrigins() 不能使用通配符 *，这也是出于安全上的考虑。
                // allowCredentials： 响应头表示是否可以将对请求的响应暴露给页面。返回true则可以，其他值均不可以。
                .allowCredentials(true)
                //allowedOrigins：允许所有的请求域名访问我们的跨域资源，可以固定单条或者多条内容，如："http://www.baidu.com"，只有百度可以访问我们的跨域资源。
                .allowedOrigins("http://localhost:8080")
                //allowedMethods：允许输入参数的请求方法访问该跨域资源服务器，如：POST、GET、PUT、OPTIONS、DELETE等。
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                //allowedHeaders：允许所有的请求header访问，可以自定义设置任意请求头信息，如："X-YAUTH-TOKEN"
                .allowedHeaders("*");
/**        前端配置
        为了让前端能够带上 cookie，我们需要通过 axios 主动开启 withCredentials 功能，即在 main.js 中添加一行
        axios.defaults.withCredentials = true
        这样，前端每次发送请求时就会带上 sessionId，shiro 就可以通过 sessionId 获取登录状态并执行是否登录的判断。
        现在还存在一个问题，即后端接口的拦截是实现了，但页面的拦截并没有实现，仍然可以通过伪造参数，绕过前端的路由限制，
        访问本来需要登录才能访问的页面。为了解决这个问题，我们可以修改 router.beforeEach 方法：
        即访问每个页面前都向后端发送一个请求，目的是经由拦截器验证服务器端的登录状态，防止上述情况的发生。**/
    }
}

/*
addViewControllers：页面跳转
    以前写SpringMVC的时候，如果需要访问一个页面，必须要写Controller类，然后再写一个方法跳转到页面，感觉好麻烦，其实重写WebMvcConfigurer中的addViewControllers方法即可达到效果了
addResourceHandlers：静态资源
    比如，我们想自定义静态资源映射目录的话，只需重写addResourceHandlers方法即可。
    addResoureHandler：指的是对外暴露的访问路径  addResourceLocations：指的是内部文件放置的目录*/
