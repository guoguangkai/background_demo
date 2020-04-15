package com.zgx.ademo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

//POJO（Plain Ordinary Java Object）即普通Java类，具有一部分getter/setter方法的那种类就可以称作POJO。实际意义就是普通的JavaBeans（简单的实体类）,作用是方便程序员使用数据库中的数据表
//JavaBean 是一种JAVA语言写成的可重用组件。符合一定规范编写的Java类.1、所有属性为private 2、提供默认构造方法3、提供getter和setter4、实现serializable接口。【便于封装重用】
/*lombok插件：
  @Data ： 注在类上，提供类的get、set、equals、hashCode、canEqual、toString方法
  @AllArgsConstructor ： 注在类上，提供类的全参构造
  @NoArgsConstructor ： 注在类上，提供类的无参构造
  @Setter ： 注在属性上，提供 set 方法
  @Getter ： 注在属性上，提供 get 方法
  @EqualsAndHashCode ： 注在类上，提供对应的 equals 和 hashCode 方法
  @Log4j/@Slf4j ： 注在类上，提供对应的 Logger 对象，变量名为 log*/
/*为了简化对数据库的操作，我们使用了 Java Persistence API（JPA）
@Entity 表示这是一个实体类
@Table(name=“user”) 表示对应的表名是 user
@JsonIgnoreProperties 因为是做前后端分离，而前后端数据交互用的是 json 格式。 那么 User 对象就会被转换为 json 数据。
        而本项目使用 jpa 来做实体类的持久化，jpa 默认会使用 hibernate, 在 jpa 工作过程中，就会创造代理类来继承 User ，
        并添加 handler 和 hibernateLazyInitializer 这两个无须 json 化的属性，所以这里需要用 JsonIgnoreProperties 把这两个属性忽略掉。*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Username.
     */
    private String username;

    /**
     * Password.
     */
    private String password;

    /**
     * Salt for encoding.
     */
    private String salt;

    /**
     * Real name.
     */
    private String name;

    /**
     * Phone number.
     */
    private String phone;

    /**
     * Email address.
     */
    private String email;

    /**
     * User status.
     */
    private boolean enabled;

    /**
     * Transient property for storing role owned by current user.
     */
    @Transient
    private List<AdminRole> roles;

    // 用于配合自定义查询的构造函数
    public User(int id,String username, String name, String phone, String email, boolean enabled) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.enabled = enabled;
    }
}
