package com.sunlands.library.service;

import com.sunlands.library.domain.BookType;

import java.util.List;

/**
 * @author : hulin
 * @date : 2018/6/14 15:57
 * @description :
 */
public interface BookTypeService {
    /**
     *
     * 功能描述: 获取所有图书类目
     *
     * @param
     * @return java.util.List<com.sunlands.library.domain.BookType>
     * @date 2018/6/14 15:58
     */
    List<BookType> getAllType();
}
