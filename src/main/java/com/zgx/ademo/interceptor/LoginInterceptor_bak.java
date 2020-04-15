package com.zgx.ademo.interceptor;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
/*写完了拦截器，但是它却并不会生效，因为我们还没有把它配置到项目中。新建 package 名为 config，新建类 MyWebConfigurer*/
/*拦截器主要用途：进行用户登录状态的拦截，日志的拦截等。*/
/*为了限制未登录状态下对核心功能页面的访问，实现登录拦截方式一（注意如果没有把前后端项目整合起来，就没有办法使用这种方式）
一个简单拦截器的逻辑如下：
1.用户访问 URL，检测是否为登录页面，如果是登录页面则不拦截
2.如果用户访问的不是登录页面，检测用户是否已登录，如果未登录则跳转到登录页面*/
//在 Springboot 我们可以直接继承拦截器的接口，然后实现 preHandle 方法。preHandle 方法里的代码会在访问需要拦截的页面时执行。
public class LoginInterceptor_bak implements HandlerInterceptor {
    //判断 session 中是否存在 user 属性，如果存在就放行，如果不存在就跳转到 login 页面。
    @Override
    public boolean preHandle (HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
/*        一个web应用对应一个ServletContext实例，这个实例是应用部署启动后，servlet容器为应用创建的。ServletContext实例包含了所有servlet共享的资源信息。
        context就是“容器”，放的就是应用程序的所有资源，要用时候就访问它，所以context里面的东西，在同一个应用程序里面是全局的；web上下文可以看成web应用的运行环境，
        一般用context名字来修饰，里面保存了web应用相关的一些设置和全局变量 。context.getContextPath() 获取web的上下文路径
        Context javaWeb中的上下文环境概念就是：一个web服务启动后的整个服务中的所有内存对象和他们之间的关系组成的一种环境
        ServletContext,是一个全局的储存信息的空间，服务器开始，其就存在，服务器关闭，其才释放。request，一个用户可有多个；session，一个用户一个；
        而servletContext，所有用户共用一个。所以，为了节省空间，提高效率，ServletContext中，要放必须的、重要的、所有用户需要共享的线程又是安全的一些信息；
        首先，对于一个web应用，其部署在web容器中，web容器提供其一个全局的上下文环境，这个上下文就是ServletContext，其为后面的spring IoC容器提供宿主环境；
        spring是怎样在web容器中启动起来的。spring的启动过程其实就是其IoC容器的启动过程，对于web程序，IoC容器启动过程即是建立上下文的过程。
        其次，在web.xml中会提供有contextLoaderListener。在web容器启动时，会触发容器初始化事件，此时contextLoaderListener会监听到这个事件，
        其contextInitialized方法会被调用，在这个方法中，spring会初始化一个启动上下文，这个上下文被称为根上下文，
        即WebApplicationContext，这是一个接口类，确切的说，其实际的实现类是XmlWebApplicationContext。这个就是spring的IoC容器，其对应的Bean定义的配置由web.xml中的context-param标签指定。
        再次，contextLoaderListener监听器初始化完毕后，开始初始化web.xml中配置的Servlet，这个servlet可以配置多个，以最常见的DispatcherServlet为例，这个servlet实际上是一个标准的前端控制器，
        用以转发、匹配、处理每个servlet请求。DispatcherServlet上下文在初始化的时候会建立自己的IoC上下文，用以持有spring mvc相关的bean。*/
     /**   String contextPath=session.getServletContext().getContextPath();**/
        /*这里使用了一个路径列表（requireAuthPages），可以在里面写下需要拦截的路径。当然也可以拦截所有路径，但会有逻辑上的问题，
        就是访问了 \login 页面，仍然会需要跳转，这样就会引发多次重定向问题。*/
        ///////////////////在注册拦截器的地方配置了拦截路径不包括index。html，这里就不需要再配置了拦截index 【他仿佛在逗我】//////////////////////////////////////////////////////////////////
      /**   String[] requireAuthPages = new String[]{
                "index",
        };**/
        /*basePath:http://localhost:8080/demo/
        getContextPath:/demo
        getServletPath:/login.jsp
        getRequestURI:/demo/login.jsp
        getRequestURL:http://localhost:8080/demo/login.jsp
        getRealPath:D:\Tomcat 6.0\webapps\demo\
        */
      /**    String uri = httpServletRequest.getRequestURI();

        uri = StringUtils.remove(uri, contextPath+"/");
        String page = uri;

        if(begingWith(page, requireAuthPages)){
            User user = (User) session.getAttribute("user");
            if(user==null) {**/
                /*客户发送一个请求到服务器，服务器匹配servlet，这都和请求转发一样，servlet处理完之后调用了sendRedirect()这个方法，
                 这个方法是response的方法，所以，当这个servlet处理完之后，看到response.senRedirect()方法，立即向客户端返回这个响应，
                 响应行告诉客户端你必须要再发送一个请求，去访问student_list.jsp，紧接着客户端受到这个请求后，立刻发出一个新的请求，
                 去请求student_list.jsp,这里两个请求互不干扰，相互独立，在前面request里面setAttribute()的任何东西，在后面的request里面都获得不了。
                 可见，在sendRedirect()里面是两个请求，两个响应。
                重定向是客户端跳转 加'/'会报错    代表的是服务器根目录  tomcat/webapps/    代表这里面的内容
                Servlet转发是内部转发  代表tomcat/webapps/项目
                最佳方式： 转发一律带'/'
                重定向 不带'/'
                */
       /**      httpServletResponse.sendRedirect("login");
                return false;
            }
        }
        return true;
    }**/

   /** private boolean begingWith(String page, String[] requiredAuthPages) {
        boolean result = false;
        for (String requiredAuthPage : requiredAuthPages) {**/
 /*         字符串以prefix开始
            StringUtils.startsWith("sssdf","");//结果是：true
            StringUtils.startsWith("sssdf","");//结果是：true
            StringUtils.startsWith("sssdf","s");//结果是：true
            StringUtils.startsWith("sssdf","ss");//结果是：true
            StringUtils.startsWith("sssdf","sss");//结果是：true
            StringUtils.startsWith("sssdf","sssdf");//结果是：true
            StringUtils.startsWith("sssdf","f");//结果是：false
            字符串以数组中的字符串开始
            StringUtils.startsWithAny("aabcde",newString[]{"g","f"});//结果是：false
            StringUtils.startsWithAny("aabcde",newString[]{"g","a"});//结果是：true
             */
        /**    if (StringUtils.startsWith(page, requiredAuthPage)) {
                result = true;
                break;
            }
        }
        return result;
    }**/

        ///////////////上面是一般的拦截验证，下面展示shrio的方式///////////////
        /*由于跨域情况下会先发出一个 options 请求试探，这个请求是不带 cookie 信息的，所以 shiro 无法获取到 sessionId，将导致认证失败
        HTTP请求方法并不是只有GET和POST，只是最常用的。还要OPTIONS等
        所以先针对OPTIONS的处理，如果是OPTIONS请求，就*/
        if (HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())) {
            //HTTP 204 No Content成功状态响应代码指示请求已成功，但客户端无需离开其当前页面。204响应默认是可缓存的
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            return true;
        }
        Subject subject = SecurityUtils.getSubject();
        // 使用 shiro 验证
        if (!subject.isAuthenticated()) {
            return false;
        }
        //RememberMe状态的不能进入敏感页面 由于目前我们并没有特殊需求，所以姑且两种状态都放行：
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            return false;
        }
        return true;
    }
    //之后，为了允许跨域的 cookie，我们需要在配置类 MyWebConfigurer 做一些修改
}
