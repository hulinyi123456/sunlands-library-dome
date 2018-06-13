package com.sunlands.library.controller;

import com.github.pagehelper.PageInfo;
import com.sunlands.library.domain.Role;
import com.sunlands.library.service.RoleService;
import com.sunlands.library.util.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @RequiresPermissions("role:listUI")
    @RequestMapping("listUI")
    public String listUI() {
        return "role/listUI";
    }

    //查看所有角色
    @RequiresPermissions("role:listUI")
    @RequestMapping("list")
    @ResponseBody
    public Result list(int offset, int limit,String search) {
        PageInfo<Role> pageInfo = this.roleService.getListByPage(offset / limit + 1, limit, search);
        return Result.succeed(pageInfo);
    }

    //查看用户拥有的角色
    @RequiresPermissions("user:setRole")
    @RequestMapping("getRoleListWithSelected/{userId}")
    @ResponseBody
    public Result getRoleListWithSelected(@PathVariable("userId")int userId) {
        List<Role> roleList = this.roleService.getRoleListWithSelectedByUserId(userId);
        return Result.succeed(roleList);
    }

    /**
     *
     * 功能描述: 设置用户权限
     *
     * @param roleId role id
     * @param permissionIds 权限id数组
     * @return com.sunlands.library.util.Result
     * @date 2018/6/13 13:59
     */
    @RequiresPermissions("role:setPermission")
    @RequestMapping("setPermission")
    @ResponseBody
    public Result setPermission(int roleId,String permissionIds) {
        this.roleService.saveRolePermission(roleId,permissionIds);
        return Result.succeed();
    }

    @RequestMapping("saveUI")
    public String saveUI(Integer roleId, HttpServletRequest request){
        if (roleId!=null){
            Role role = this.roleService.getRoleById(roleId);
            request.setAttribute("role",role);
        }
        return "role/saveUI";
    }

    /**
     *
     * 功能描述: 保存或者更新角色
     *
     * @param role 角色对象
     * @return com.sunlands.library.util.Result
     * @date 2018/6/13 14:19
     */
    @RequiresPermissions(value={"role:add","role:update"})
    @RequestMapping("save")
    public String save(Role role){
        if(role.getRoleId()!=null){
            this.roleService.update(role);
        }else{
            this.roleService.save(role);
        }
        return "redirect:/role/listUI";
    }


}
