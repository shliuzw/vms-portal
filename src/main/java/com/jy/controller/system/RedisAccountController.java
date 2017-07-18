package com.jy.controller.system;

import com.jy.common.utils.FileUtil;
import com.jy.common.utils.webpage.PageData;
import com.jy.controller.BaseController;
import com.jy.entity.system.Account;
import com.jy.entity.system.Content;
import com.jy.service.redis.RedisService;
import com.jy.service.system.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/account")
public class RedisAccountController extends BaseController<Content> {

	@Autowired
	private RedisService accountRedisService;
	@Autowired
	private AccountService accountService;
	
	@RequestMapping(value = "/delAccount",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> delAccount(){
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = this.getPageData();
		String token = pd.getString("token");
		accountRedisService.delMapField("token", token);
		Account ret2 = accountService.findFormatByToken(token);
		if (ret2 !=null) {
			accountRedisService.delMapField("username", ret2.getLoginName());
			ret2.setUpdateTime(new Date());
			accountService.sysResetToken(ret2);
		}
		String errInfo = "success";
		map.put("result", errInfo);
		return map;
	}

}
