package com.zgx.ademo.controller;

import com.zgx.ademo.entity.Book;
import com.zgx.ademo.service.BookService;
import com.zgx.ademo.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class LibraryController {
    @Autowired
    BookService bookService;

    @GetMapping("/api/books")
    public List<Book> list() throws Exception {
        return bookService.list();
    }

    @PostMapping("/api/books")
    public Book addOrUpdate(@RequestBody Book book) throws Exception {
        bookService.addOrUpdate(book);
        return book;
    }

    @PostMapping("/api/delete")
    public void delete(@RequestBody Book book) throws Exception {
        bookService.deleteById(book.getId());
    }


    @GetMapping("/api/categories/{cid}/books")
    public List<Book> listByCategory(@PathVariable("cid") int cid) throws Exception {
        if (0 != cid) {
            return bookService.listByCategory(cid);
        } else {
            return list();
        }
    }

    //文件上传
    //MultipartFile是spring类型，代表HTML中form data方式上传的文件，包含二进制数据+文件名称。
/*    在项目中我们使用MultipartFile来接收postman传过来的文件,把MultipartFile类型的文件转化为File类型的文件，
    最后将文件写入InputStreamReader，进而逐行读取文件内容并写入list中。*/
    @CrossOrigin
    @PostMapping("api/covers")
    public String coversUpload(MultipartFile file) throws Exception {
        String folder = "D:/workspace/img";
        //使用指定路径（上面的所得到文件夹的绝对路径）构造一个File对象
/*        // 1. 通过指定文件路径实例化
        File file1 = new File("D:/txt/a.txt");
        // 2. param1 文件根路径字符串； param2 文件名称
        File file2 = new File("D:/txt","a.txt");
        // 3. param1 文件对象； param2 文件名称
        File file3 = new File(new File("D:/txt"), "a.txt");
        // 4. 通过资源标识符实例化
        File file4 = new File(new URI("file:///D:/txt/a.txt"));*/
       /*  String name = "AAAA.txt";
        String lujing = "1"+"/"+"2";//定义路径
        File a = new File(lujing,name);
        那么，a的意义就是“1/2/AAAA.txt”。
        这里a是File，但是File这个类在Java里表示的不只是文件，虽然File在英语里是文件的意思。Java里，File至少可以表示文件或文件夹
        也就是说，在“1/2/AAAA.txt”真正出现在磁盘结构里之前，它既可以表示这个文件，也可以表示这个路径的文件夹。那么，
        如果没有getParentFile(),直接执行a.mkdirs()，就是说，创建“1/2/AAAA.txt”代表的文件夹，也就是“1/2/AAAA.txt/”，在此之后，
        执行a.createNewFile()，试图创建a文件，然而以a为名的文件夹已经存在了，所以createNewFile()实际是执行失败的。
        所以，这里，你想要创建的是“1/2/AAAA.txt”这个文件。在创建AAAA.txt之前，必须要1/2这个目录存在。所以，要得到1/2，就要用a.getParentFile()，
        然后要创建它，也就是a.getParentFile().mkdirs()。在这之后，a作为文件所需要的文件夹大概会存在了（有特殊情况会无法创建的，这里不考虑），
        就执行a.createNewFile()创建a文件。*/
        File imageFolder = new File(folder);
        //StringUtils.getRandomString(6) 产生6位长度的随机字符串。 file.getOriginalFilename()是得到上传时的文件名。
        File f = new File(imageFolder, StringUtils.getRandomString(6) + file.getOriginalFilename()
                .substring(file.getOriginalFilename().length() - 4));
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        try {
            //transferto()方法，是springmvc封装的方法，用于图片上传时，把内存中图片写入磁盘
            file.transferTo(f);
            //这个 URL 的前缀是我们自己构建的，还需要把它跟我们设置的图片资源文件夹，即 D:/workspace/img 对应起来。在 config\MyWebConfigurer 中添加代码
            String imgURL = "http://localhost:8443/api/file/" + f.getName();
            return imgURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }



}
