package com.zgx.ademo.dao;

import com.zgx.ademo.entity.Book;
import com.zgx.ademo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookDAO extends JpaRepository<Book,Integer> {
    //把 category 对象的 id 属性作为Book的 cid 进行了查询。
    List<Book> findAllByCategory(Category category);
    List<Book> findAllByTitleLikeOrAuthorLike(String keyword1, String keyword2);
}
