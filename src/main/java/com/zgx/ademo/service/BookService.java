package com.zgx.ademo.service;

import com.zgx.ademo.dao.BookDAO;
import com.zgx.ademo.entity.Book;
import com.zgx.ademo.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*这个 Service 提供了四个功能，分别是查出所有书籍、增加或更新书籍、通过 id 删除书籍和通过分类查出书籍。
        */
@Service
public class BookService {
    @Autowired
    BookDAO bookDAO;
    @Autowired
    CategoryService categoryService;

    public List<Book> list() {
       // Sort sort = new Sort(Sort.Direction.DESC, "id");
       // return bookDAO.findAll(sort);
        return bookDAO.findAll();
    }

    public void addOrUpdate(Book book) {
       // 这里注意一下 save() 方法的作用是，当主键存在时更新数据，当主键不存在时插入数据。
        bookDAO.save(book);
    }

    public void deleteById(int id) {
        bookDAO.deleteById(id);
    }

    public List<Book> listByCategory(int cid) {
        Category category = categoryService.get(cid);
      //  Sort sort = new Sort(Sort.Direction.DESC, "id");
        return bookDAO.findAllByCategory(category);
    }
}
