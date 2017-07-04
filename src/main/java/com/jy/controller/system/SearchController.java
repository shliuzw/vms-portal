package com.jy.controller.system;

import com.jy.common.utils.FileUtil;
import com.jy.common.utils.HttpUtil;
import com.jy.common.utils.base.Const;
import com.jy.common.utils.webpage.PageData;
import com.jy.controller.BaseController;
import com.jy.entity.system.Content;
import com.jy.entity.system.ContentShort;
import com.jy.service.redis.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.*;


@Controller
@RequestMapping("/search/")
public class SearchController extends BaseController<Object>{

	@Autowired
	private RedisService contentRedisService;
	/**
	 * 请求登录，验证用户
	 */
	@RequestMapping(value="/opensearch" ,produces="application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> search()throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = this.getPageData();
		String errInfo = "";

		StringBuffer url = new StringBuffer(Const.confMap.get("search.server.baseurl"));
		url.append("?ct=101&");
		url.append("ctVer=1.0").append("&");
		url.append("isDebug=0").append("&");
		url.append("coreName=spring&fields=0");

		String type = pd.getString("type");
		String keyword = pd.getString("keyword");
		String pageSize = pd.getString("pageSize");
		String pageStart = pd.getString("pageStart");
		String order = pd.getString("order");
		if (StringUtils.isBlank(type)){
			errInfo = "type is null.";
			map.put("result", errInfo);
			return map;
		}else if ("0".equals(type)){ // 搜索词
			if (StringUtils.isBlank(keyword)){
				errInfo = "type is 0, keyword is not null.";
				map.put("result", errInfo);
				return map;
			}
		}else if ("1".equals(type)){ // 类目过滤
			url.append("&").append("type=").append(type);
		}else if (!"1".equals(type)){
			errInfo = "type is not 1,type : " + type;
			map.put("result", errInfo);
			return map;
		}
		if (StringUtils.isNoneBlank(pageSize)){
			url.append("&").append("pageSize=").append(pageSize);
		}
		if (StringUtils.isBlank(pageStart)){
			errInfo = "pageStart is null.";
			map.put("result", errInfo);
			return map;
		}else {
			url.append("&").append("pageStart=").append(pageStart);
		}
		if (StringUtils.isNoneBlank(order)){
			url.append("&").append("order=").append(order);
		}

		String retXml = HttpUtil.get(url.toString(), false);
		logger.debug("===retXml : "+retXml);
		/**
		 * 将retXml转成document解析，获取所有的searchResult.searchId属性，
		 * 然后通过id到redis获取内容明细
		 */
		Document document = DocumentHelper.parseText(retXml);
		Element root = document.getRootElement();
		Node countN = root.selectSingleNode("//searchResults/@count");
		String count = countN.getText();
		List contList = root.selectNodes("//searchResult");// 获取所有的子集
		Iterator iterCont = contList.iterator();
		List<ContentShort> csList = new ArrayList<ContentShort>();
		while(iterCont.hasNext()) {
			ContentShort contShort = new ContentShort();
			Element eleCont = (Element) iterCont.next();
			Node contId = eleCont.selectSingleNode("searchId");
			contShort.setContId(contId.getText());
			// 从redis获取内容明细
			Content cont = (Content) contentRedisService.getContentMap(contShort.getContId());
			if(cont == null){ // 没有命中，从xml数据源获取成功，并添加到缓存
				String contPath = FileUtil.idToName(Long.parseLong(contShort.getContId())) + "/" + contShort.getContId() + ".xml";
				String absContPath = System.getProperty("xml.base.path")+contPath;
				cont = this.parseContXml(absContPath);
				if (cont == null) continue;
				contentRedisService.setContentMap(contShort.getContId(), cont); // 增加到redis
			}
			contShort.setCategory(cont.getCategory());
			contShort.setH16imagePath(cont.getH16image());
			contShort.setV2imagePath(cont.getV2image());
			contShort.setLatestNum(cont.getLatestNum());
			contShort.setName(cont.getName());
			contShort.setProvider(cont.getProvider());
			contShort.setTotalNum(cont.getTotalNum());
			csList.add(contShort);
		}
		if (StringUtils.isBlank(errInfo)){
			errInfo = "success";
		}
		map.put("result", errInfo);
		map.put("count",count);
		map.put("contents",csList);
		return map;
	}
}
