package com.zgx.ademo.dao;

import com.zgx.ademo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*Data Access Object（数据访问对象，DAO）即用来操作数据库的对象，这个对象可以是我们自己开发的，也可以是框架提供的。这里我们通过继承 JpaRepository 的方式构建 DAO。*/
//需要类型化为实体类【User】，ID需要实体类User中Id（我定义的Id类型是【Integer】）的类型
public interface UserDAO extends JpaRepository<User, Integer> {
    //由于使用了 JPA，无需手动构建 SQL 语句，而只需要按照规范提供方法的名字即可实现对数据库的增删改查。
    //如 findByUsername，就是通过 username 字段查询到对应的行，并返回给 User 类。
    User findByUsername(String username);

    User getByUsernameAndPassword(String username,String password);
    //JPA 默认查询出来的就是全量的数据，如果想指定字段，就需要自定义查询语句了。这是 JPQL 的写法，也可以写原生的 SQL 语句，指定 nativeQuery = true 即可。
    @Query(value = "select new User(u.id,u.username,u.name,u.phone,u.email,u.enabled) from User u")
    List<User> list();
}
