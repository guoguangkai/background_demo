package com.zgx.ademo.service;

import com.zgx.ademo.dao.JotterArticleDAO;
import com.zgx.ademo.entity.JotterArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class JotterArticleService {
    @Autowired
    JotterArticleDAO jotterArticleDAO;

    public Page list(int page, int size) {
       // Sort sort = new Sort(Sort.Direction.DESC, "id");
        return jotterArticleDAO.findAll(PageRequest.of(page, size));
    }

    public JotterArticle findById(int id) {
        return jotterArticleDAO.findById(id);
    }

    public boolean addOrUpdate(JotterArticle article) {
        try {
            jotterArticleDAO.save(article);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    public boolean delete(int id) {
        try {
            jotterArticleDAO.deleteById(id);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

}
