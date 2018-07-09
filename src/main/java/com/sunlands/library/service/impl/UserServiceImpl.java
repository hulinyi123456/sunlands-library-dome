package com.sunlands.library.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sunlands.library.domain.User;
import com.sunlands.library.mapper.UserMapper;
import com.sunlands.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Override
    public User findUserByUserName(String userName) {
        return userMapper.getUserByUserName(userName);
    }

    @Override
    public void deleteBatchByIds(String[] idsStr) {

    }

    @Override
    public PageInfo<User> getListByPage(int currentNum, int pageSize, String name) {

        PageHelper.startPage(currentNum, pageSize);
        List<User> userList = userMapper.getByPage();

        return new PageInfo<>(userList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRole(int userId, String roleIds) {
        //先将用户拥有角色信息删除
        this.userMapper.deleteUserRoleByUserId(userId);
        //再重新保存用户的角色信息
        if (roleIds!=null&&!"".equals(roleIds)){
            String[] ids = roleIds.split(",");
            List<Map<String,Integer>> infoList = new ArrayList<>(ids.length);
            Map<String,Integer> info;
            for(String roleId:ids){
                info = new HashMap<>(16);
                info.put("userId",userId);
                info.put("roleId",Integer.valueOf(roleId));
                infoList.add(info);
            }
            this.userMapper.saveUserRole(infoList);
        }
    }

    @Override
    public User getById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public void update(User user) {
        this.userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void save(User user) {
        this.userMapper.insertSelective(user);
    }

    @Override
    public List<User> getAllUser() {
        return userMapper.getByPage();
    }

    @Override
    public List<User> getUserWithoutBook() {
        return userMapper.getUserWithoutBook();
    }
}
