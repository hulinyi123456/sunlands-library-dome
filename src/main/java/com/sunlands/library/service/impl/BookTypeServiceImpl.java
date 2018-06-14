package com.sunlands.library.service.impl;

import com.sunlands.library.domain.BookType;
import com.sunlands.library.mapper.BookTypeMapper;
import com.sunlands.library.service.BookTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : hulin
 * @date : 2018/6/14 15:59
 * @description :
 */
@Service("BookTypeService")
public class BookTypeServiceImpl implements BookTypeService {
    @Autowired
    private BookTypeMapper bookTypeMapper;

    @Override
    public List<BookType> getAllType() {
        return this.bookTypeMapper.getAllType();
    }
}
