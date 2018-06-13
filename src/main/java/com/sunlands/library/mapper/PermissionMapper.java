package com.sunlands.library.mapper;

import com.sunlands.library.domain.Permission;

import java.util.List;

public interface PermissionMapper {

    /**
     * 通过 用户ID 获取其对应的权限
     * @param userId
     * @return
     */
    List<Permission> getPermissionList(Integer userId);

    int deleteByPrimaryKey(Integer permissionId);

    int insert(Permission record);

    int insertSelective(Permission record);

    Permission selectByPrimaryKey(Integer permissionId);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    /**
     *
     * 功能描述: 获取所有权限列表，分页功能由pageHelper完成
     *
     * @param
     * @return java.util.List<com.sunlands.library.domain.Permission>
     * @date 2018/6/8 16:28
     */
    List<Permission> getByPage();

    /**
     *
     * 功能描述: 获取角色权限
     *
     * @param roleId role id
     * @return java.util.List<com.sunlands.library.domain.Permission>
     * @date 2018/6/8 16:00
     */
    List<Permission> getPermissionListByRoleId(Integer roleId);

}