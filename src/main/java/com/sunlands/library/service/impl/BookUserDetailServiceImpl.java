package com.sunlands.library.service.impl;

import com.sunlands.library.domain.BookInfo;
import com.sunlands.library.domain.BookUserDetail;
import com.sunlands.library.mapper.BookInfoMapper;
import com.sunlands.library.mapper.BookUserDetailMapper;
import com.sunlands.library.service.BookUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author : hulin
 * @date : 2018/6/14 20:08
 * @description :
 */
@Service("bookUserDetailService")
public class BookUserDetailServiceImpl implements BookUserDetailService {

    @Autowired
    private BookUserDetailMapper bookUserDetailMapper;
    @Autowired
    private BookInfoMapper bookInfoMapper;


    @Override
    @CacheEvict(value = "book",allEntries = true)
    public void saveDetail(BookUserDetail bookUserDetail) {
        bookUserDetail.setBorrowTime(new Date());
        bookUserDetailMapper.insertSelective(bookUserDetail);
        //修改图书状态为借出
        BookInfo bookInfo = new BookInfo();
        bookInfo.setbId(bookUserDetail.getBookId());
        bookInfo.setStatus((byte) 1);
        bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
    }
}
