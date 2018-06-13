package com.sunlands.library.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sunlands.library.domain.BookInfo;
import com.sunlands.library.mapper.BookInfoMapper;
import com.sunlands.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : hulin
 * @date : 2018/6/8 19:34
 * @description : book服务接口实现类，使用redis缓存
 */
@CacheConfig(cacheNames = "book")
@Service("bookService")
public class BookServiceImpl implements BookService {

    @Autowired
    private BookInfoMapper bookInfoMapper;

    @Override
    @Cacheable
    public PageInfo<BookInfo> getBookListByPage(int currentNum, int pageSize, String search) {

        PageHelper.startPage(currentNum, pageSize);
        List<BookInfo> bookInfos = bookInfoMapper.getAllBooks();
        bookInfos.forEach(bookInfo -> {
            StringBuffer author=new StringBuffer();
            JSONObject jsonObject = JSONObject.parseObject(bookInfo.getAuthor());
            Set<Map.Entry<String,Object>> authorSet =  jsonObject.entrySet();
            authorSet.forEach(a->
                author.append(a.getValue()+"，")
            );
            bookInfo.setAuthor(author.toString().substring(0,author.length()-1)+" 著");
        });

        return new PageInfo<>(bookInfos);
    }
}