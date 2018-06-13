package com.sunlands.library.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sunlands.library.domain.Permission;
import com.sunlands.library.mapper.PermissionMapper;
import com.sunlands.library.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("PermissionService")
public class PermissionServiceImpl implements PermissionService {
    @Autowired(required = false)
    private PermissionMapper permissionMapper;

    @Override
    public Map<String, List<Permission>> getPermissionMapByUserId(Integer userId) {
        List<Permission> permissionList = this.permissionMapper.getPermissionList(userId);

        List<Permission> menuList = new ArrayList<>();
        Map<Integer, Permission> map = new HashMap<>();

        // 筛选目录
        for (Permission permission : permissionList) {
            if (permission.getType() != 3 && permission.getPid() == 0L) {
                map.put(permission.getPermissionId(), permission);
                menuList.add(permission);
            }
        }

        // 封装菜单
        for (Permission permission : permissionList) {
            if (permission.getType() != 3 && map.get(permission.getPid()) != null) {
                Permission parent = map.get(permission.getPid());
                parent.getChildren().add(permission);
            }
        }

        Map<String,List<Permission>> resultMap = new HashMap<>(2);
        resultMap.put("menuList", menuList);
        resultMap.put("permissionList", permissionList);

        return resultMap;
    }

    @Override
    public PageInfo<Permission> getListByPage(int currentNum, int pageSize, String search) {
        PageHelper.startPage(currentNum, pageSize);
        List<Permission> permissionList = permissionMapper.getByPage();
        //使用PageInfo对象返回便于分页
        return new PageInfo<>(permissionList);
    }

    @Override
    public List<Permission> getPermissionListWithChecked(Integer roleId) {
        List<Permission> list = permissionMapper.getPermissionListByRoleId(roleId);
        List<Permission> permissionList = permissionMapper.getByPage();

        //如果是目标角色的权限则选中
        permissionList.forEach(p->
            list.forEach(l->{
                if(p.getPid().equals(l.getPid())){
                    p.setChecked(true);
                }
            }));

        return permissionList;
    }
}
