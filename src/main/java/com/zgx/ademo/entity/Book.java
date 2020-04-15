package com.zgx.ademo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
//persistence 持久化
@Entity
@Table(name = "book")
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@Data
public class Book {
    @Id //@Id 标注用于声明一个实体类的属性映射为数据库的主键列
    @GeneratedValue(strategy = GenerationType.IDENTITY) //主键默认数据库自增
    @Column(name = "id") //指定持久化属性映射到数据库表的列
    int id;

    @ManyToOne //数据模型：一个种类可以有多个本书，而一本书只能对应一个种类。
    // 使用@ManyToOne来映射多对一关系、使用@JoinColum来映射外键，并用name属性来映射外键的列名。
    //相当于category是book表的一个外键，数据库列名为cid。
    @JoinColumn(name="cid")
    private Category category;

    String cover;
    String title;
    String author;
    String date;
    String press;
    String abs;
}



