package com.sunlands.library.mapper;

import com.sunlands.library.domain.BookInfo;

import java.util.List;

public interface BookInfoMapper {
    int deleteByPrimaryKey(Integer bId);

    int insert(BookInfo record);

    int insertSelective(BookInfo record);

    BookInfo selectByPrimaryKey(Integer bId);

    int updateByPrimaryKeySelective(BookInfo record);

    int updateByPrimaryKey(BookInfo record);

    /**
     *
     * 功能描述: 获取所有书籍信息
     *
     * @param
     * @return java.util.List<com.sunlands.library.domain.BookInfo>
     * @date 2018/6/8 19:36
     */
    List<BookInfo> getAllBooks();
}