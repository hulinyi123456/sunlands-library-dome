package com.sunlands.library.service;

import com.github.pagehelper.PageInfo;
import com.sunlands.library.domain.Role;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface RoleService {
    List<Role> getRolesByUserId(Integer userId);

    PageInfo<Role> getListByPage(int currentNum, int limit, String search);

    List<Role> getRoleListWithSelectedByUserId(int userId);

    /**
     *
     * 功能描述: 保存角色的权限
     *
     * @param roleId 角色ID
     * @param permissionIds 权限ID字符串
     * @return void
     * @date 2018/6/8 17:57
     */
    @Transactional(rollbackFor = Exception.class)
    void saveRolePermission(int roleId, String permissionIds);

    /**
     *
     * 功能描述: 保存角色
     *
     * @param role
     * @return com.sunlands.library.util.Result
     * @date 2018/6/13 14:23
     */
    void save(Role role);

    /**
     *
     * 功能描述:通过id获取角色对象
     *
     * @param roleId
     * @return com.sunlands.library.domain.Role
     * @date 2018/6/13 15:11
     */
    Role getRoleById(Integer roleId);

    /**
     *
     * 功能描述: 更新角色
     *
     * @param role
     * @return void
     * @date 2018/6/13 15:14
     */
    void update(Role role);
}
