package com.sunlands.library.service;

import com.sunlands.library.domain.BookUserDetail;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : hulin
 * @date : 2018/6/14 20:06
 * @description :
 */
public interface BookUserDetailService {
    /**
     *
     * 功能描述: 保存借阅信息
     *
     * @param bookUserDetail
     * @return void
     * @date 2018/6/14 20:06
     */
    @Transactional(rollbackFor = Exception.class)
    void saveDetail(BookUserDetail bookUserDetail);
}
