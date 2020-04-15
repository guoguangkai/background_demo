package com.zgx.ademo.service;

import com.zgx.ademo.dao.CategoryDAO;
import com.zgx.ademo.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryDAO categoryDAO;
    //这里对查询的结果做了个排序以及条件判断。
    public List<Category> list() {
        //Sort sort = new Sort(Sort.Direction.DESC,"id");
        return categoryDAO.findAll();
    }

    public Category get(int id) {
        //从 Java 8引入的一个很有趣的特性是 Optional  类。Optional 类主要解决的问题是臭名昭著的空指针异常（NullPointerException）
        //findById(id)返回值是 abstract optional<Category>  本质上，这是一个包含有可选值的包装类，这意味着 Optional 类既可以含有对象也可以为空。
        //Optional.orElse()如果有值则返回该值，否则返回传递给它的参数值
        Category c= categoryDAO.findById(id).orElse(null);
        return c;
    }
}
