package com.sunlands.library.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sunlands.library.domain.BookInfo;
import com.sunlands.library.domain.BookUserDetail;
import com.sunlands.library.mapper.BookInfoMapper;
import com.sunlands.library.mapper.BookTypeMapper;
import com.sunlands.library.mapper.UserMapper;
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
    @Autowired
    private BookTypeMapper bookTypeMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Cacheable
    public PageInfo<BookInfo> getBookListByPage(int currentNum, int pageSize, String search) {

        PageHelper.startPage(currentNum, pageSize);
        List<BookInfo> bookInfos = bookInfoMapper.getAllBooks();
        bookInfos.forEach(bookInfo -> {
            StringBuffer author=new StringBuffer();
            //添加类目名称
            bookInfo.setTypeName(this.bookTypeMapper.selectByPrimaryKey(bookInfo.getTypeId()).getName());
            //获取借阅信息
            BookUserDetail bookUserDetail =bookInfo.getCurrentDetail();
            if (bookUserDetail!=null){
                bookUserDetail.setBorrower(userMapper.selectByPrimaryKey(bookUserDetail.getUserId()).getUserName());
                String renewal = bookUserDetail.getRenewal();
                if(renewal!=null){
                    JSONObject jRenewal = JSONObject.parseObject(renewal);
                    renewal = (String) jRenewal.get("reTime");
                    bookUserDetail.setRenewal(renewal);
                }
                bookInfo.setCurrentDetail(bookUserDetail);
            }
            //解析json获得作者信息
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
