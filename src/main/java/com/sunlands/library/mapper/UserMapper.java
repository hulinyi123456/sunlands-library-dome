package com.sunlands.library.mapper;

import com.sunlands.library.domain.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    User getUserByUserName(String userName);

    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> getByPage();

    void deleteUserRoleByUserId(int userId);

    void saveUserRole(List<Map<String,Integer>> infoList);

    List<User> getUserWithoutBook();
}