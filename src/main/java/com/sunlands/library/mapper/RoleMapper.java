package com.sunlands.library.mapper;

import com.sunlands.library.domain.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper {

    List<Role> selectByUserId(Integer uesrId);

    int deleteByPrimaryKey(Integer roleId);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Role> getByPage();

    /**
     *
     * 功能描述: 删除角色所有权限，解绑
     *
     * @param roleId 角色ID
     * @return void
     * @date 2018/6/8 18:13
     */
    void deleteRolePermissionByRoleId(int roleId);

    /**
     *
     * 功能描述: 保存角色权限
     *
     * @param infoList 角色权限列表
     * @return void
     * @date 2018/6/8 18:15
     */
    void saveRolePermission(List<Map<String,Integer>> infoList);
}