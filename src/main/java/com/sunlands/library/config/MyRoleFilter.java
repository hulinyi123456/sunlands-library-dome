package com.sunlands.library.config;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 修改角色控制为or的关系
 * @author hulin
 */
public class MyRoleFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object o) throws Exception {
        String[] roles = (String[])o;

        //没有角色限制，有权限访问
        if (roles == null || roles.length == 0) {
            return true;
        }

        Subject subject = getSubject(request, response);
        for (String role : roles) {
            if(subject.hasRole(role)){
                return true;
            }
        }
        return false;
    }

}
