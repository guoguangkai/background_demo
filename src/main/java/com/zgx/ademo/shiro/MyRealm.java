package com.zgx.ademo.shiro;

import com.zgx.ademo.entity.User;
import com.zgx.ademo.service.AdminPermissionService;
import com.zgx.ademo.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

//shiro中自定义realm一般继承AuthorizingRealm，然后实现getAuthenticationInfo和getAuthorizationInfo方法，来完成登录和权限的验证。
public class MyRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private AdminPermissionService adminPermissionService;
    // 简单重写获取授权信息方法
   /* 该方法主要是用于当前登录用户授权。
      1. 调用SecurityUtils.getSubject().isPermitted(String str)方法时会调用。
      2. 在@Controller 上@RequiresRoles("admin")在方法上加注解的时候调用
      3. [@shiro.hasPermission name = "admin"][/@shiro.hasPermission]或者<shiro:hasPermission name="admin"></shiro:hasPermission>
     在页面上加shiro标签的时候，即进这个页面的时候扫描到有这个标签的时候调用*/
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 获取当前用户的所有权限
        String username = principalCollection.getPrimaryPrincipal().toString();
        Set<String> permissions = adminPermissionService.listPermissionURLsByUser(username);

        // 将权限放入授权信息中
        SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();
        s.setStringPermissions(permissions);
        return s;
    }

    // 获取认证信息，即根据 token 中的用户名从数据库中获取密码、盐等并返回
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = token.getPrincipal().toString();
        User user = userService.findByUsername(userName);
        String passwordInDB = user.getPassword();
        String salt = user.getSalt();
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, passwordInDB, ByteSource.Util.bytes(salt), getName());
        return authenticationInfo;
    }
}
