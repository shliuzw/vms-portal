package com.jy.controller.system;

import com.jy.common.utils.base.Tools;
import com.jy.common.utils.redis.ListPage;
import com.jy.common.utils.webpage.PageData;
import com.jy.controller.BaseController;
import com.jy.entity.system.Comment;
import com.jy.service.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/comment")
public class RedisCommentController  extends BaseController<Comment> {

	@Autowired
	private RedisService commentRedisService;
	
	@RequestMapping(value = "/publish",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> publish(){
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = this.getPageData();
		String errInfo = "";
		String id = pd.getString("id");
		String msg = pd.getString("msg");
		String accountNo = pd.getString("accountNo");
		try {
			Integer.parseInt(id);
		}catch (Exception ex){
			ex.printStackTrace();
			errInfo = "error:id is error!";
		}
		if (msg.length() > 50){
			errInfo = "error:msg's length is error!";
		}
		if (!Tools.checkMobileNumber(accountNo)){
			errInfo = "error:accountNo is not mobile no!";
		}
		if (StringUtils.isBlank(errInfo)){
			Comment comment = new Comment();
			comment.setContId(Integer.parseInt(id));
			comment.setLoginName(accountNo);
			comment.setDetail(msg);
			comment.setPublishTime(new Date());
			commentRedisService.leftPush(id, comment);
			errInfo = "success";
		}
		map.put("result", errInfo);
		return map;
	}
	@RequestMapping(value = "/list",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> getList(){
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = this.getPageData();
		String errInfo = "";
		String id = pd.getString("id");
		long start = Long.parseLong(pd.getString("start"));
		long end = Long.parseLong(pd.getString("end"));
		try {
			Integer.parseInt(id);
		}catch (Exception ex){
			ex.printStackTrace();
			errInfo = "error:id is error!";
		}
		List<Comment> comments = null;
		if (StringUtils.isBlank(errInfo)){
			comments = (List<Comment>) commentRedisService.range(id, start,end);
			errInfo = "success";
		}
		map.put("result", errInfo);
		map.put("comments",comments);
		return map;
	}
}
