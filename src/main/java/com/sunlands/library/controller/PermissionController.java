package com.sunlands.library.controller;

import com.github.pagehelper.PageInfo;
import com.sunlands.library.domain.Permission;
import com.sunlands.library.service.PermissionService;
import com.sunlands.library.util.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author hulin
 */
@Controller
@RequestMapping("permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    
    /**
     *
     * 功能描述: 获取权限列表页
     *
     * @param: []
     * @return: java.lang.String
     * @auther: hulin
     * @date: 2018/5/28 14:19
     */
    @RequiresPermissions("permission:listUI")
    @RequestMapping("listUI")
    public String listUI() {
        return "permission/listUI";
    }

    /**
     *
     * 功能描述: 异步获取分页返回权限列表
     *
     * @param offset 当前行数
     * @param limit 每页行数
     * @param search 搜索条件
     * @return com.sunlands.library.util.Result
     * @date 2018/6/8 16:16
     */
    @RequiresPermissions("permission:listUI")
    @RequestMapping("list")
    @ResponseBody
    public Result list(int offset, int limit,String search) {
        PageInfo<Permission> pageInfo = this.permissionService.getListByPage(offset / limit + 1, limit, search);
        return Result.succeed(pageInfo);
    }

    /**
     *
     * 功能描述: 获取权限列表，并标记出角色已有的权限
     *
     * @param roleId role id
     * @return com.sunlands.library.util.Result
     * @auther hulin
     * @date 2018/6/8 15:38
     */
    @RequiresPermissions("role:setPermission")
    @RequestMapping("getPermissionListWithChecked/{roleId}")
    @ResponseBody
    public Result getPermissionListWithChecked(@PathVariable("roleId") int roleId){
        List<Permission> permissionList = permissionService.getPermissionListWithChecked(roleId);
        return Result.succeed(permissionList);
    }

}
