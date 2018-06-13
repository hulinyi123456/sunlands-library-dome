package com.sunlands.library.config;

import com.github.pagehelper.util.StringUtil;
import com.sunlands.library.domain.Permission;
import com.sunlands.library.domain.Role;
import com.sunlands.library.domain.User;
import com.sunlands.library.service.PermissionService;
import com.sunlands.library.service.RoleService;
import com.sunlands.library.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        // （目录+菜单+按钮）
        List<Permission> permissionList = user.getPermissionList();

        for (Permission permission : permissionList) {
            if (StringUtil.isNotEmpty(permission.getCode())) {
                info.addStringPermission(permission.getCode());
            }
        }

        for (Role  role:user.getRoleList()){
            info.addRole(role.getName());
        }

        return info;
    }

    /**
     *
     * 功能描述: 验证用户
     *
     * @param: [authenticationToken] 令牌
     * @return: org.apache.shiro.authc.AuthenticationInfo
     * @auther: hulin
     * @date: 2018/5/30 14:32
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        User user = userService.findUserByUserName(token.getUsername());
        if (user==null){
            return null;
        }
        System.out.println(user.getUserId()+user.getUserName()+user.getPassword());

        // 通过 userId 获取该用户拥有的所有权限，返回值根据自己要求设置，并非固定值。
        Map<String,List<Permission>> permissionMap = this.permissionService.getPermissionMapByUserId(user.getUserId());
        List<Role> roleList = this.roleService.getRolesByUserId(user.getUserId());

        // （目录+菜单，分层级）
        user.setMenuList(permissionMap.get("menuList"));
        // （目录+菜单+按钮）
        user.setPermissionList(permissionMap.get("permissionList"));

        user.setRoleList(roleList);


        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user,user.getPassword(),getName());
        return authenticationInfo;
    }
}
