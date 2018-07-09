package com.sunlands.library.service;

import com.github.pagehelper.PageInfo;
import com.sunlands.library.domain.User;

import java.util.List;

public interface UserService {

    //通过用户名获取用户
    User findUserByUserName(String userName);

    void deleteBatchByIds(String[] idsStr);

    //返回分页list
    PageInfo<User> getListByPage(int currentNum, int pageSize, String name);

    //保存设置的用户角色
    void saveUserRole(int userId, String roleIds);

    //通过ID获取用户
    User getById(Integer id);

    //更新用户信息
    void update(User user);

    //新增用户
    void save(User user);

    /**
     *
     * 功能描述: 获取所有用户
     *
     * @param
     * @return java.util.List<com.sunlands.library.domain.User>
     * @date 2018/6/14 19:27
     */
    List<User> getAllUser();

    /**
     *
     * 功能描述: 获取没有借书的用户
     *
     * @param
     * @return java.util.List<com.sunlands.library.domain.User>
     * @date 2018/7/9 15:55
     */
    List<User> getUserWithoutBook();
}
