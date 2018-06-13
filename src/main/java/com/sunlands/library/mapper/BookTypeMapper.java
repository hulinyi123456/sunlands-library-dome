package com.sunlands.library.mapper;

import com.sunlands.library.domain.BookType;

public interface BookTypeMapper {
    int deleteByPrimaryKey(Integer typeId);

    int insert(BookType record);

    int insertSelective(BookType record);

    BookType selectByPrimaryKey(Integer typeId);

    int updateByPrimaryKeySelective(BookType record);

    int updateByPrimaryKey(BookType record);
}