package com.sunlands.library.service;

import com.github.pagehelper.PageInfo;
import com.sunlands.library.domain.Permission;

import java.util.List;
import java.util.Map;

public interface PermissionService {
    /**
     * 获取用户可以查看的菜单列表（封装2个list,(菜单+目录) 和 (菜单+目录+按钮)）
     * @param userId
     * @return
     */
    Map<String, List<Permission>> getPermissionMapByUserId(Integer userId);

    PageInfo<Permission> getListByPage(int currentNum, int pageSize, String search);

    /**
     *
     * 功能描述: 获取权限列表，并标记出角色已有的权限
     *
     * @param  roleId role id
     * @return  java.util.List<com.sunlands.library.domain.Permission>
     * @date  2018/6/8 15:40
     */
    List<Permission> getPermissionListWithChecked(Integer roleId);
}
