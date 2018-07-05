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

    /**
     *
     * 功能描述: 打开新建/编辑图书页面
     *
     * @param model 
     * @param bId 图书ID
     * @return java.lang.String
     * @date 2018/7/4 16:55
     */
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

    /**
     *
     * 功能描述: 保存新建图书信息，如果传入图书ID则保存编辑信息
     *
     * @param book 图书对象
     * @return java.lang.String
     * @date 2018/7/4 16:57
     */
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

    /**
     *
     * 功能描述: 图书借阅
     *
     * @param userId 借阅者ID
     * @param bId 图书ID
     * @return com.sunlands.library.util.Result
     * @date 2018/7/4 17:00
     */
    @RequestMapping("borrow")
    @ResponseBody
    public Result borrow(Integer userId,Integer bId){
        BookUserDetail bookUserDetail = new BookUserDetail();
        bookUserDetail.setBookId(bId);
        bookUserDetail.setUserId(userId);
        this.bookUserDetailService.saveDetail(bookUserDetail);
        return Result.succeed();
    }

    /**
     *
     * 功能描述: 归还/续期图书
     *
     * @param book 保存图书ID的对象
     * @param detail 保存借阅详情的ID的对象
     * @return com.sunlands.library.util.Result
     * @date 2018/7/4 17:49
     */
    @RequestMapping("return")
    @ResponseBody
    public Result setBookStatus(BookInfo book,BookUserDetail detail,Integer operation){
        //1是归还，2是延期
        this.bookService.setBookStatus(book,detail,operation);
        return Result.succeed();
    }

}
