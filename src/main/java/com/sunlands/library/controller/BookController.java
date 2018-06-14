package com.sunlands.library.controller;

import com.github.pagehelper.PageInfo;
import com.sunlands.library.domain.BookInfo;
import com.sunlands.library.domain.BookType;
import com.sunlands.library.domain.BookUserDetail;
import com.sunlands.library.service.BookService;
import com.sunlands.library.service.BookTypeService;
import com.sunlands.library.service.BookUserDetailService;
import com.sunlands.library.util.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author : hulin
 * @date : 2018/6/8 19:17
 * @description :
 */
@Controller
@RequestMapping("book")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookTypeService bookTypeService;
    @Autowired
    private BookUserDetailService bookUserDetailService;

    /**
     *
     * 功能描述: 打开book列表
     *
     * @param
     * @return java.lang.String
     * @date 2018/6/8 19:21
     */
    @RequiresPermissions("book:listUI")
    @RequestMapping("listUI")
    public String listUI() {
        return "book/listUI";
    }

    /**
     *
     * 功能描述: 获取所有book
     *
     * @param offset 位移行数
     * @param limit 每页最大行数
     * @param search 搜索条件
     * @return com.sunlands.library.util.Result
     * @date 2018/6/8 19:21
     */
    @RequiresPermissions("book:listUI")
    @RequestMapping("list")
    @ResponseBody
    public Result list(int offset, int limit, String search) {
        PageInfo<BookInfo> pageInfo = this.bookService.getBookListByPage(offset / limit + 1, limit, search);
        return Result.succeed(pageInfo);
    }

    @RequestMapping("saveUI")
    public String saveUI(Model model,Integer bId){
        if (bId!=null){
            BookInfo bookInfo = this.bookService.getBookById(bId);
            model.addAttribute("book",bookInfo);
        }
        List<BookType> bookTypes = this.bookTypeService.getAllType();
        model.addAttribute("bookTypes",bookTypes);
        return "book/saveUI";
    }

    @RequiresPermissions(value={"book:add"})
    @RequestMapping("save")
    public String save(BookInfo book){
        if(book.getbId()!=null){
            this.bookService.update(book);
        }else {
            this.bookService.save(book);
        }
        return "redirect:/book/listUI";
    }

    @RequestMapping("borrow")
    @ResponseBody
    public Result borrow(Integer userId,Integer bId){
        BookUserDetail bookUserDetail = new BookUserDetail();
        bookUserDetail.setBookId(bId);
        bookUserDetail.setUserId(userId);
        this.bookUserDetailService.saveDetail(bookUserDetail);
        return Result.succeed();
    }
}
