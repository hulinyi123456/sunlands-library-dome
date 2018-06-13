package com.sunlands.library.service;

import com.github.pagehelper.PageInfo;
import com.sunlands.library.domain.BookInfo;

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
}
