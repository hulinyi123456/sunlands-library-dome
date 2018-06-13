package com.sunlands.library.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sunlands.library.domain.Role;
import com.sunlands.library.mapper.RoleMapper;
import com.sunlands.library.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired(required = false)
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRolesByUserId(Integer userId) {
        return roleMapper.selectByUserId(userId);
    }

    @Override
    public PageInfo<Role> getListByPage(int currentNum, int pageSize, String search) {
        PageHelper.startPage(currentNum, pageSize);
        List<Role> roleList = roleMapper.getByPage();

        return new PageInfo<>(roleList);

    }

    @Override
    public List<Role> getRoleListWithSelectedByUserId(int userId) {
        List<Role> roles = roleMapper.selectByUserId(userId);
        List<Role> roleList = roleMapper.getByPage();

        //将已拥有的角色选中
        for (Role role:roleList){
            for(Role r:roles){
                if(role.getRoleId()==r.getRoleId()){
                    role.setSelected(true);
                }
            }
        }
        return roleList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRolePermission(int roleId, String permissionIds) {
        //先删除角色所有权限，解绑
        this.roleMapper.deleteRolePermissionByRoleId(roleId);

        //再绑定所有权限
        if(permissionIds!=null && !"".equals(permissionIds)){
            String[] ids = permissionIds.split(",");
            List<Map<String,Integer>> infoList = new ArrayList<>(ids.length);
            Map<String,Integer> info;
            for(String id:ids){
                info = new HashMap<>(16);
                info.put("roleId",roleId);
                info.put("permissionId",Integer.valueOf(id));
                infoList.add(info);
            }
            this.roleMapper.saveRolePermission(infoList);
        }
    }

    @Override
    public void save(Role role) {
        this.roleMapper.insert(role);
    }

    @Override
    public Role getRoleById(Integer roleId) {
        return this.roleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public void update(Role role) {
        this.roleMapper.updateByPrimaryKeySelective(role);
    }
}
