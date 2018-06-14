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
import org.springframework.cache.annotation.CacheEvict;
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
        bookInfos.forEach(bookInfo -> setBook(bookInfo));
        return new PageInfo<>(bookInfos);
    }

    @Override
    @CacheEvict(value = "book",allEntries = true,beforeInvocation = true)
    public void save(BookInfo book) {
        this.bookInfoMapper.insertSelective(this.setAuthor(book));
    }

    @Override
    @CacheEvict(value = "book",allEntries = true,beforeInvocation = true)
    public void update(BookInfo book) {
        this.bookInfoMapper.updateByPrimaryKeySelective(this.setAuthor(book));
    }

    @Override
    public BookInfo getBookById(Integer bId) {
        BookInfo bookInfo = setBook(this.bookInfoMapper.selectByPrimaryKey(bId));
        bookInfo.setAuthor(bookInfo.getAuthor().substring(0,bookInfo.getAuthor().length()-2));
        return bookInfo;
    }

    /**
     *
     * 功能描述: 将作者修改成符合json格式的字符串
     *
     * @param book
     * @return com.sunlands.library.domain.BookInfo
     * @date 2018/6/14 17:38
     */
    public BookInfo setAuthor(BookInfo book){
        if(book.getAuthor()!=null){
            String authors = book.getAuthor();
            StringBuffer js =new StringBuffer("{");
            js.append("\"author\":\""+authors+"\""+"}");
            book.setAuthor(js.toString());
        }
        return book;
    }

    /**
     *
     * 功能描述: 图书添加类目名称和借阅信息
     *
     * @param bookInfo
     * @return com.sunlands.library.domain.BookInfo
     * @date 2018/6/14 17:38
     */
    public BookInfo setBook(BookInfo bookInfo){
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
        return bookInfo;
    }
}
