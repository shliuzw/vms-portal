package com.jy.controller.system;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jy.common.ajax.AjaxRes;
import com.jy.common.utils.base.Const;
import com.jy.common.utils.security.AccountShiroUtil;
import com.jy.common.utils.webpage.PageData;
import com.jy.controller.BaseController;
import com.jy.entity.system.Account;

@Controller
@RequestMapping("/backstage/")
public class BackstageController extends BaseController<Object>{
	
	/**
	 * 访问系统首页
	 */
	@RequestMapping("index")
	public String index(Model model){
		//shiro获取用户信息
		Account currentAccount=AccountShiroUtil.getCurrentUser();
		model.addAttribute("currentAccount", currentAccount);		
		return "/system/index";
	}
	

	@RequestMapping("adv")
	public String advUI(Model model) {	
		return "/system/adv/adv";
	}
	
	@RequestMapping("404")
	public String errorlistUI(Model model){	
		return "/system/error/404";
	}
	/**
	 * 没权限页面
	 * @param model
	 * @return
	 */
	@RequestMapping("noAuthorized")
	public String noAuthorizedUI(Model model){	
		return Const.NO_AUTHORIZED_URL;
	}
}
