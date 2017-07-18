package com.jy.controller.system;

import com.jy.common.utils.DateUtils;
import com.jy.common.utils.FileUtil;
import com.jy.common.utils.webpage.PageData;
import com.jy.controller.BaseController;
import com.jy.entity.system.Content;
import com.jy.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/content")
public class RedisContentController extends BaseController<Content> {

	@Autowired
	private RedisService contentRedisService;
	
	@RequestMapping(value = "/putCont",produces="application/json;charset=UTF-8")
	@ResponseBody
	@Deprecated
	public Map<String,Object> putCont(){
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = this.getPageData();
		String errInfo = "";
		Content content = new Content();
		content.setActor("actor1");
		content.setCategory("category1");
		content.setContId("100001");
		content.setContType("contType1");
		contentRedisService.setContentMap("100001", content);
		map.put("result", errInfo);
		return map;
	}

	@RequestMapping(value = "/delCont",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> delCont(){
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = this.getPageData();
		String id = pd.getString("id");
		String errInfo = "success";
		contentRedisService.delContentMapField(id);
		map.put("result", errInfo);
		return map;
	}

	@RequestMapping(value = "/playAction",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> incrCont(){
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = this.getPageData();
		String id = pd.getString("id");
		String errInfo = "";
		try {
			String curDate = DateUtils.getDate(DateUtils.parsePatterns[0]);
			String counterKey = "clickamount::"+curDate;
			long idCounter = contentRedisService.incrementCont(id,1l);
//			System.out.println("===counter " + id + " : " + contentRedisService.getIncrValue(id));
			contentRedisService.setMap(counterKey, id, idCounter);
//			Map<String,Long> keyMap = contentRedisService.getAllMap(counterKey);
//			Set<String> keySet = keyMap.keySet();// map中的所有key在set中存放着，可以通过迭代set的方式 来获得key
//			Iterator<String> iter = keySet.iterator();
//			while(iter.hasNext()){
//				String key = iter.next();
//				Long value = keyMap.get(key);
//				System.out.println("key2: "+key+"  value2: "+value);
//			}
			errInfo = "success";
			map.put("counter",idCounter);
		}catch (Exception e){
			e.printStackTrace();
			errInfo = "error counter.";
		}
		map.put("result", errInfo);
		map.put("id",id);
		return map;
	}

	/**
	 * app通过内容ID，获取内容对象。
	 * 如果redis没有找到内容对象，需要从数据源中加载；
	 * 如果数据源中的信息变化，如何更新redis中的内容对象？当管理端更新内容时，调用portal提供删除redis内容对象API服务
	 * @return
	 */
	@RequestMapping(value = "/getCont",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String,Object> getCont(){
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = this.getPageData();
		String id = pd.getString("id");
		String errInfo = "";
		String contPath = "";
		Content content = (Content) contentRedisService.getContentMap(id);
		if(content == null){ // 没有命中，从xml数据源获取成功，并添加到缓存
			contPath = FileUtil.idToName(Long.parseLong(id)) + "/" + id + ".xml";
			String absContPath = System.getProperty("xml.base.path")+contPath;
			content = this.parseContXml(absContPath);
			if (content == null){
				errInfo = contPath + " is not find.";
			}else {
				contentRedisService.setContentMap(id, content); // 增加到redis
				contentRedisService.expire(id, 10);
				errInfo = "success";
			}
		}else {// 命中
			errInfo = "success";
		}
		map.put("result", errInfo);
		map.put("content",content);
		return map;
	}


}
