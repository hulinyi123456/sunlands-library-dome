package com.sunlands.library.controller;

import com.github.pagehelper.PageInfo;
import com.sunlands.library.domain.User;
import com.sunlands.library.service.UserService;
import com.sunlands.library.util.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequiresPermissions("user:listUI")
	@RequestMapping("listUI")
	public String listUI() {
		return "user/listUI";
	}

	//显示所有用户
	@RequiresPermissions("user:listUI")
	@RequestMapping("list")
	@ResponseBody
	public Result list(int offset, int limit,String search) {

		PageInfo<User> pageInfo = this.userService.getListByPage(offset / limit + 1, limit, search);
		return Result.succeed(pageInfo);
	}

	//打开新增页面
	@RequestMapping("saveUI")
	public String saveUI(Integer id,HttpServletRequest request) {
		if (id != null) {
			User user = this.userService.getById(id);
			System.out.println(user.getStatus());
			if (user != null) {
				request.setAttribute("user", user);
			}
		}
		return "user/saveUI";
	}

	//保存设置的角色信息
	@RequiresPermissions("user:setRole")
	@RequestMapping("setRole")
	@ResponseBody
	public Result setRole(int userId,String roleIds) {

		this.userService.saveUserRole(userId,roleIds);

		return Result.succeed();
	}

	@RequiresPermissions(value={"user:add","user:update"})
	@RequestMapping("save")
	public String add(User user) {
		if (user.getUserId() != null) {
			user.setUpdateTime(new Date());
			this.userService.update(user);
		} else {
			user.setCreateTime(new Date());
			user.setUpdateTime(user.getCreateTime());
			user.setPassword("123456");
			this.userService.save(user);
		}
		return "/user/listUI";
	}

}
