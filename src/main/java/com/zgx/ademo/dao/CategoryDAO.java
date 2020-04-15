package com.zgx.ademo.dao;

import com.zgx.ademo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryDAO extends JpaRepository<Category, Integer> {
    /*这个 DAO 不需要额外构造的方法，JPA 提供的默认方法就够用了。*/
}
