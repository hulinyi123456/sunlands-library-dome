package com.sunlands.library.service;

import com.github.pagehelper.PageInfo;
import com.sunlands.library.domain.BookInfo;
import com.sunlands.library.domain.BookUserDetail;

/**
 * @author : hulin
 * @date : 2018/6/8 19:24
 * @description :
 */

public interface BookService {

    /**
     *
     * 功能描述: 获取所有书籍信息,使用pageHelper分页
     *
     * @param currentNum 当前页
     * @param pageSize 每页行数
     * @param search 搜索条件
     * @return java.util.List<java.awt.print.Book>
     * @date 2018/6/8 19:31
     */

    PageInfo<BookInfo> getBookListByPage(int currentNum, int pageSize, String search);

    /**
     *
     * 功能描述: 新增图书
     *
     * @param book
     * @return void
     * @date 2018/6/14 16:25
     */
    void save(BookInfo book);

    /**
     *
     * 功能描述: 更新图书信息
     *
     * @param book
     * @return void
     * @date 2018/6/14 16:26
     */
    void update(BookInfo book);

    /**
     *
     * 功能描述: 通过id获取图书信息
     *
     * @param bId
     * @return com.sunlands.library.domain.BookInfo
     * @date 2018/6/14 17:21
     */
    BookInfo getBookById(Integer bId);

    /**
     *
     * 功能描述: 修改图书借阅状态和借阅详情状态
     *
     * @param book 保存有图书ID的对象
     * @param detail 保存了借阅详情ID的对象
     * @param operation 操作命令，1是归还，2是延期
     * @return void
     * @date 2018/7/4 17:52
     */
    void setBookStatus(BookInfo book, BookUserDetail detail,Integer operation);
}
