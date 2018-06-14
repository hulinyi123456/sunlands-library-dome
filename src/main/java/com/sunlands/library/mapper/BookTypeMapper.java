package com.sunlands.library.mapper;

import com.sunlands.library.domain.BookType;

import java.util.List;

public interface BookTypeMapper {
    int deleteByPrimaryKey(Integer typeId);

    int insert(BookType record);

    int insertSelective(BookType record);

    BookType selectByPrimaryKey(Integer typeId);

    int updateByPrimaryKeySelective(BookType record);

    int updateByPrimaryKey(BookType record);

    /**
     *
     * 功能描述:获取所有图书类目
     *
     * @param
     * @return java.util.List<com.sunlands.library.domain.BookType>
     * @date 2018/6/14 16:01
     */
    List<BookType> getAllType();
}