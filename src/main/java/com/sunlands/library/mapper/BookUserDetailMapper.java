package com.sunlands.library.mapper;

import com.sunlands.library.domain.BookUserDetail;

public interface BookUserDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BookUserDetail record);

    int insertSelective(BookUserDetail record);

    BookUserDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BookUserDetail record);

    int updateByPrimaryKey(BookUserDetail record);
}